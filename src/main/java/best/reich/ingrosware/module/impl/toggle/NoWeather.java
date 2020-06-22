package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.other.GetRainStrengthEvent;

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
