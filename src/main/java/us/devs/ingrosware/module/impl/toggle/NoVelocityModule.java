package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.entity.UpdateEvent;
import us.devs.ingrosware.event.impl.network.PacketEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.setting.annotation.Setting;

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
