package us.devs.ingrosware.module.impl.toggle;

import org.lwjgl.input.Keyboard;
import us.devs.ingrosware.gui.hud.GuiHudEditor;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Toggleable(label = "HudEditor", category = ModuleCategory.RENDER, color = 0xff33ffff, bind = Keyboard.KEY_INSERT, hidden = true)
public class HudEditorModule extends ToggleableModule {
    private GuiHudEditor hudEditor;

    @Override
    public void onState() {
        super.onState();
        if(hudEditor == null) {
            hudEditor = new GuiHudEditor();
        }
        mc.displayGuiScreen(hudEditor);
        toggle();
    }
}
