package us.devs.ingrosware.module.impl.toggle;

import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.entity.UpdateEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
@Toggleable(label = "NoPush",category = ModuleCategory.PLAYER,color = 0xff777777,bind = Keyboard.KEY_NONE)
public class NoPushModule extends ToggleableModule {
    private float savedReduction;
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        mc.player.entityCollisionReduction = 1.0F;
    }

    @Override
    public void onState() {
        savedReduction = mc.player != null ? mc.player.entityCollisionReduction : 0.0f;
    }

    @Override
    public void onDisable() {
        mc.player.entityCollisionReduction = savedReduction;
    }
}
