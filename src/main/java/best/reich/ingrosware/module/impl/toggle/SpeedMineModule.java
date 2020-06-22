package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Mode;
import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.event.impl.other.ClickBlockEvent;
import best.reich.ingrosware.event.impl.other.DamageBlockEvent;
import best.reich.ingrosware.event.impl.other.ResetBlockRemovingEvent;
import best.reich.ingrosware.event.impl.other.TraceEntityEvent;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
@Toggleable(label = "SpeedMine", category = ModuleCategory.OTHER, color = 0xff7272AE, bind = Keyboard.KEY_NONE)
public class SpeedMineModule extends ToggleableModule {
    @Setting("No-Trace")
    public boolean noTrace = true;

    @Setting("Mode")
    @Mode({"PACKET", "DAMAGE","INSTANCE"})
    public String mode = "PACKET";
    @Setting("Reset")
    public boolean reset = true;
    @Setting("DoubleBreak")
    public boolean doubleBreak = false;

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        setSuffix(mode);
        if (event.getType() == EventType.PRE) {
            mc.playerController.blockHitDelay = 0;

            if (this.reset && mc.gameSettings.keyBindUseItem.isKeyDown())
                mc.playerController.isHittingBlock = false;
        }
    }

    @Subscribe
    public void resetBlockDamage(ResetBlockRemovingEvent event) {
        if (mc.world == null || mc.player == null) return;

        if (this.reset)
            event.setCancelled(true);
    }

    @Subscribe
    public void clickBlock(ClickBlockEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (this.reset) {
            if (mc.playerController.curBlockDamageMP > 0.1f)
                mc.playerController.isHittingBlock = true;
        }
    }

    @Subscribe
    public void damageBlock(DamageBlockEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (mc.world.getBlockState(event.getPos()).getBlock() == Blocks.PORTAL) return;
        if (canBreak(event.getPos())) {

            if (this.reset)
                mc.playerController.isHittingBlock = false;

            switch (this.mode) {
                case "PACKET":
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                    break;
                case "DAMAGE":
                    if (mc.playerController.curBlockDamageMP >= 0.7f) {
                        mc.playerController.curBlockDamageMP = 1.0f;
                    }
                    break;
                case "INSTANT":
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                    mc.playerController.onPlayerDestroyBlock(event.getPos());
                    mc.world.setBlockToAir(event.getPos());
                    break;
            }
        }

        if (this.doubleBreak) {
            final BlockPos above = event.getPos().add(0, 1, 0);

            if (canBreak(above) && mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5f) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.getFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.getFacing()));
                mc.playerController.onPlayerDestroyBlock(above);
                mc.world.setBlockToAir(above);
            }
        }
    }

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    @Subscribe
    public void onEntityTrace(TraceEntityEvent event) {
        event.setCancelled(noTrace && mc.player.getHeldItemMainhand() != ItemStack.EMPTY && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe);
    }
}
