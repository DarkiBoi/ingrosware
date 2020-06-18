package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
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
 * @since 6/14/2020
 **/
@Toggleable(label = "Sprint2", category = ModuleCategory.MOVEMENT,color = 0xff72B190,bind = Keyboard.KEY_NONE)
public class Sprint2Module extends ToggleableModule {

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