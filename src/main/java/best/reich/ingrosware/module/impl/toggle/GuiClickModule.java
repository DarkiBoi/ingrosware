package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.click.GuiClick;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Toggleable(label = "GuiClick", category = ModuleCategory.RENDER, color = 0xff33feff, bind = Keyboard.KEY_INSERT, hidden = true)
public class GuiClickModule extends ToggleableModule {
    private GuiClick click;

    @Override
    public void onState() {
        super.onState();
        if(click == null) {
            click = new GuiClick();
            click.init();
        }
        mc.displayGuiScreen(click);
        toggle();
    }
}
