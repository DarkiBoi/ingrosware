package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.entity.UpdateEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.setting.annotation.Clamp;
import us.devs.ingrosware.setting.annotation.Setting;


/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
@Toggleable(label = "Offhand", category = ModuleCategory.COMBAT, color = 0xffff3f0f, bind = Keyboard.KEY_NONE)
public class OffhandModule extends ToggleableModule {
    @Clamp(minimum = "1", maximum = "22")
    @Setting("Health")
    public int health = 20;

    @Setting("ToggleTotem")
    public boolean toggletotem = true;

    @Setting("CrystalCheck")
    public boolean crystalCheck = true;

    @Setting("HoleCheck")
    public boolean holeCheck = true;

    @Clamp(minimum = "1", maximum = "22")
    @Setting("HoleHealth")
    public int holeHealth = 8;

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen instanceof GuiContainer)
            return;
        if (!shouldTotem()) {
            if (!(mc.player.getHeldItemOffhand() != ItemStack.EMPTY && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)) {
                final int slot = getCrystalSlot() < 9 ? getCrystalSlot() + 36 : getCrystalSlot();
                if (getCrystalSlot() != -1) {
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                }
            }
        } else if (!(mc.player.getHeldItemOffhand() != ItemStack.EMPTY && mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)) {
            final int slot = getTotemSlot() < 9 ? getTotemSlot() + 36 : getTotemSlot();
            if (getTotemSlot() != -1) {
                mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    private boolean nearPlayers() {
        return mc.world.playerEntities.stream().anyMatch(e -> e != mc.player && e.getEntityId() != -1488 && !IngrosWare.INSTANCE.getFriendManager().isFriend(e.getGameProfile().getId()) && mc.player.getDistanceToEntity(e) <= 10);
    }

    private boolean shouldTotem() {
        if (holeCheck && isInHole(mc.player)) {
            return (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= holeHealth || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !nearPlayers() || mc.player.fallDistance >= 3 || (crystalCheck && !isCrystalsAABBEmpty());
        }
        return (mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= health || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA || !nearPlayers() || mc.player.fallDistance >= 3 || (crystalCheck && !isCrystalsAABBEmpty());
    }

    private boolean isEmpty(BlockPos pos) {
        return mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityEnderCrystal).count() == 0;
    }

    private boolean isCrystalsAABBEmpty() {
        return isEmpty(mc.player.getPosition().add(1, 0, 0)) && isEmpty(mc.player.getPosition().add(-1, 0, 0)) && isEmpty(mc.player.getPosition().add(0, 0, 1)) && isEmpty(mc.player.getPosition().add(0, 0, -1)) && isEmpty(mc.player.getPosition());
    }

    private boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    private boolean isBlockValid(BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }

    private boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }

        return true;
    }

    private boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }

        return true;
    }

    private boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

    int getCrystalSlot() {
        int crystalSlot = -1;
        for (int i = 45; i > 0; i--) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                crystalSlot = i;
                break;
            }
        }
        return crystalSlot;
    }

    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > 0; i--) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }

    @Override
    public void onState() {
        if (toggletotem && IngrosWare.INSTANCE.getModuleManager().getModule("AutoTotem").isEnabled())
            ((AutoTotemModule) IngrosWare.INSTANCE.getModuleManager().getModule("AutoTotem")).toggle();
    }

    @Override
    public void onDisable() {
        if (toggletotem && !IngrosWare.INSTANCE.getModuleManager().getModule("AutoTotem").isEnabled())
            ((AutoTotemModule) IngrosWare.INSTANCE.getModuleManager().getModule("AutoTotem")).toggle();
    }
}