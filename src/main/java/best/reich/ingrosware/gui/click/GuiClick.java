package best.reich.ingrosware.gui.click;

import best.reich.ingrosware.gui.click.frame.Frame;
import best.reich.ingrosware.gui.click.frame.impl.MainFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
public class GuiClick extends GuiScreen {
    private final ArrayList<Frame> frames = new ArrayList<>();

    public void init() {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        getFrames().add(new MainFrame("IngrosWare", (scaledResolution.getScaledWidth() >> 1) - 200, (scaledResolution.getScaledHeight() >> 1) - 150,400,300));
        getFrames().forEach(Frame::initFrame);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        getFrames().forEach(frame -> frame.drawScreen(mouseX,mouseY,partialTicks));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        getFrames().forEach(frame -> frame.keyTyped(typedChar,keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        getFrames().forEach(frame -> frame.mouseClicked(mouseX,mouseY,mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        getFrames().forEach(frame -> frame.mouseReleased(mouseX,mouseY,mouseButton));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        getFrames().forEach(Frame::onGuiClosed);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }
}
