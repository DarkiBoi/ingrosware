package us.devs.ingrosware.module.impl.toggle;

import org.lwjgl.input.Keyboard;
import us.devs.ingrosware.gui.click.GuiClick;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;

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
