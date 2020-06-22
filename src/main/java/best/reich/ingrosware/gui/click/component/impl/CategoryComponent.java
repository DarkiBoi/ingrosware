package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import best.reich.ingrosware.gui.click.component.Component;
import best.reich.ingrosware.gui.click.frame.impl.MainFrame;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
public class CategoryComponent extends Component {
    private final MainFrame mainFrame;
    private final ModuleCategory moduleCategory;
    private float scrollY;

    public CategoryComponent(MainFrame mainFrame, ModuleCategory moduleCategory, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(moduleCategory.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.mainFrame = mainFrame;
        this.moduleCategory = moduleCategory;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), getWidth(), getHeight(), 0.5f, getMainFrame().getSelectedCatergory() == getModuleCategory() ? 0xff505050 : 0xff353535, 0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(), getPosX() + getWidth() / 2 - RenderUtil.getStringWidth(getLabel()) / 2, getPosY() + 3, getMainFrame().getSelectedCatergory() == getModuleCategory() ? -1 : 0xff858585);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight()))
            getMainFrame().setSelectedCatergory(getModuleCategory());
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public ModuleCategory getModuleCategory() {
        return moduleCategory;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }
}
