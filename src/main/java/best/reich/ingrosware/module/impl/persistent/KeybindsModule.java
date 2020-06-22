package best.reich.ingrosware.module.impl.persistent;

import best.reich.ingrosware.module.annotation.Persistent;
import best.reich.ingrosware.module.types.PersistentModule;
import best.reich.ingrosware.module.types.ToggleableModule;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.KeyPressEvent;
import best.reich.ingrosware.macro.Macro;
import best.reich.ingrosware.module.ModuleCategory;

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
