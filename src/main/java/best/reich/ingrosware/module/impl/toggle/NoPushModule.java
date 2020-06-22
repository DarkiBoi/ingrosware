package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.module.ModuleCategory;

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
