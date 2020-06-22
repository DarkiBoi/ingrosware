package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.network.PacketEvent;
import best.reich.ingrosware.mixin.accessors.ISPacketPosLook;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/14/2020
 **/
@Toggleable(label = "NoRotate", category = ModuleCategory.OTHER,color = 0xfff33f00,bind = Keyboard.KEY_NONE)
public class NoRotateModule extends ToggleableModule {

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.world == null) return;
        if (event.getType() == EventType.POST) {
            if (event.getPacket() instanceof SPacketPlayerPosLook && mc.player != null) {
                final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                ((ISPacketPosLook) packet).setYaw(mc.player.rotationYaw);
                ((ISPacketPosLook) packet).setPitch(mc.player.rotationPitch);
            }
        }
    }
}
