package us.devs.ingrosware.module.impl.persistent;

import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.other.KeyPressEvent;
import us.devs.ingrosware.macro.Macro;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Persistent;
import us.devs.ingrosware.module.types.PersistentModule;
import us.devs.ingrosware.module.types.ToggleableModule;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Persistent(label = "Keybinds", category = ModuleCategory.OTHER)
public class KeybindsModule extends PersistentModule {

    @Subscribe
    public void onKey(KeyPressEvent event) {
        for(ToggleableModule toggleableModule : IngrosWare.INSTANCE.getModuleManager().getToggles()) {
            if(toggleableModule.getBind() == event.getKey())
                toggleableModule.getKeybind().getTask().execute();
        }

        for(Macro macro : IngrosWare.INSTANCE.getMacroManager().getValues()) {
            if(macro.getKey() == event.getKey())
                macro.getKeybind().getTask().execute();
        }
    }

}
