package best.reich.ingrosware.event.impl.render;

import tcb.bces.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class DisplayGuiEvent extends Event {

    private GuiScreen screen;

    public DisplayGuiEvent(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public void setScreen(GuiScreen screen) {
        this.screen = screen;
    }
}