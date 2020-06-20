package us.devs.ingrosware.module.impl.toggle;

import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.other.GetRainStrengthEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;

/**
 * @author TBM
 * @since 6/20/2020
 **/
@Toggleable(label = "NoWeather", category = ModuleCategory.RENDER, color = 0xff777788, bind = Keyboard.KEY_NONE)
public class NoWeather extends ToggleableModule {

    @Subscribe
    public void onGetRainStrength(GetRainStrengthEvent event) {
        event.setCancelled();
    }

}
