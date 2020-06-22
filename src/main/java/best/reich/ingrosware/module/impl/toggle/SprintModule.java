package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/14/2020
 **/
@Toggleable(label = "Sprint", category = ModuleCategory.MOVEMENT,color = 0xff72B190,bind = Keyboard.KEY_NONE)
public class SprintModule extends ToggleableModule {

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (canSprint(mc.player)) mc.player.setSprinting(true);
    }

    private boolean canSprint(EntityPlayerSP player) {
        return !player.isSprinting() && !player.isSneaking() && player.movementInput.moveForward > 0.0f &&
                (player.getFoodStats().getFoodLevel() >= 6f || player.capabilities.allowFlying) &&
                !player.isPotionActive(MobEffects.BLINDNESS);
    }
}