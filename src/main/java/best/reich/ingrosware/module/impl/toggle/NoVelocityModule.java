package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.network.PacketEvent;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/14/2020
 **/
@Toggleable(label = "NoVelocity", category = ModuleCategory.COMBAT,color = 0x717171,bind = Keyboard.KEY_NONE)
public class NoVelocityModule extends ToggleableModule {

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.player == null) return;
        if (event.getType() == EventType.POST) {
            if ((event.getPacket() instanceof SPacketEntityVelocity) && (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()))
                event.setCancelled(true);
            if (event.getPacket() instanceof SPacketExplosion)
                event.setCancelled(true);
        }
    }
}
