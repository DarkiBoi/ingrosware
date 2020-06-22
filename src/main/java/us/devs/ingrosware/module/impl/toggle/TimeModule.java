package us.devs.ingrosware.module.impl.toggle;

import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.other.WorldTimeEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.setting.annotation.Mode;
import us.devs.ingrosware.setting.annotation.Setting;

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
