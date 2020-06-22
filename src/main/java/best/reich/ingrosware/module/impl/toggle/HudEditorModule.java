package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.hud.GuiHudEditor;
import best.reich.ingrosware.module.ModuleCategory;

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
