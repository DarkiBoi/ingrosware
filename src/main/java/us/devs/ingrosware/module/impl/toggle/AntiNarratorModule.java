package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.other.SetGameOptionEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;


/**
 * @author TBM
 * @since 6/20/2020
 **/
@Toggleable(label = "AntiNarrator", category = ModuleCategory.OTHER, color = 0xff33feff, bind = Keyboard.KEY_NONE)
public class AntiNarratorModule extends ToggleableModule {

    @Subscribe
    public void onSetGameOption(SetGameOptionEvent event) {
        if (event.getSettingOption() == GameSettings.Options.NARRATOR) event.setCancelled();
    }

}
