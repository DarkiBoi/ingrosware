package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.other.SetGameOptionEvent;


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
