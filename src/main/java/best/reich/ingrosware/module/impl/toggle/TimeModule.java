package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.event.impl.other.WorldTimeEvent;
import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Mode;
import best.reich.ingrosware.setting.annotation.Setting;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;

/**
 * @author TBM
 * @since 6/22/2020
 **/
@Toggleable(label = "Time", category = ModuleCategory.RENDER, color = 0xff777799, bind = Keyboard.KEY_NONE)
public class TimeModule extends ToggleableModule {

    @Setting("Time")
    @Mode({"DAY", "NIGHT", "SUNRISE", "SUNSET"})
    public String time = "DAY";

    @Subscribe
    public void onWorldTime(WorldTimeEvent event) {
        event.setCancelled();
        event.setWorldTime(timeToLong(time));
    }

    public long timeToLong(String chosentime) {
        switch (chosentime) {
            case "DAY":
                return 1000L;
            case "NIGHT":
                return 13000L;
            case "SUNRISE":
                return 23000L;
            case "SUNSET":
                return 12000L;
        }
        return 1000L;
    }

}
