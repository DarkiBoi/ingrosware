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
    private boolean once;

    @Setting("AAC")
    public boolean aac;

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null || !aac) return;
        if (event.getType() == EventType.PRE) {
            if (mc.player.hurtTime == 9 & once & mc.player.onGround) {
                once = false;
            }
            if (mc.player.hurtTime > 0 & mc.player.hurtTime <= 7) {
                mc.player.motionX *= 0.5;
                mc.player.motionZ *= 0.5;
            }
            if (mc.player.hurtTime == 5) {
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                once = true;
            }
            if (mc.player.hurtTime == 4) {
                final double playerYaw = Math.toRadians(mc.player.rotationYaw);
                mc.player.setPosition(mc.player.posX - (Math.sin(playerYaw) * 0.05), mc.player.posY, mc.player.posZ);
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.player == null || aac) return;
        if (event.getType() == EventType.POST) {
            if ((event.getPacket() instanceof SPacketEntityVelocity) && (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId())) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onState() {
        once = false;
    }
}
