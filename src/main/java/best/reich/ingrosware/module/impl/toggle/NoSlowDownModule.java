package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.entity.UpdateInputEvent;
import best.reich.ingrosware.event.impl.network.PacketEvent;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/14/2020
 **/
@Toggleable(label = "NoSlowDown", category = ModuleCategory.MOVEMENT,color = 0x666666,bind = Keyboard.KEY_NONE)
public class NoSlowDownModule extends ToggleableModule {
    @Setting("NCP")
    public boolean ncp = true;

    @Subscribe
    public void onInputUpdate(UpdateInputEvent event) {
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.movementInput.moveStrafe *= 5;
            mc.player.movementInput.moveForward *= 5;
        }
    }

    @Subscribe
    public void onPre(PacketEvent event) {
        if (event.getType() == EventType.PRE && event.getPacket() instanceof CPacketPlayer && ncp && mc.player.isActiveItemStackBlocking() && mc.player.onGround && !IngrosWare.INSTANCE.getModuleManager().getToggleByName("Speed").isEnabled() && (mc.player.moveStrafing != 0 || mc.player.moveForward != 0) && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = 0.02;
            ((CPacketPlayer)event.getPacket()).y = (((CPacketPlayer)event.getPacket()).y + 0.3);
        }
    }
}
