package us.devs.ingrosware.gui.click.component.impl;

import net.minecraft.client.Minecraft;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.gui.click.component.Component;
import us.devs.ingrosware.gui.click.frame.impl.MainFrame;
import us.devs.ingrosware.module.IModule;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.math.MouseUtil;
import us.devs.ingrosware.util.render.RenderUtil;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
public class ModuleComponent extends Component {
    private final MainFrame mainFrame;
    private final IModule module;
    private boolean extended;
    public ModuleComponent(MainFrame mainFrame, IModule module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.mainFrame = mainFrame;
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), getWidth(), getHeight(), 0.5f, getModule().isEnabled() ? 0xff505050:0xff353535, 0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(), getPosX() + getWidth() / 2 - (Minecraft.getMinecraft().fontRenderer.getStringWidth(getLabel()) >> 1), getPosY() + 3, getModule().isEnabled() ?-1:0xff858585);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        if (hovered) {
            switch (mouseButton) {
                case 0:
                    if (getModule() instanceof ToggleableModule)
                        ((ToggleableModule) getModule()).setState(!getModule().isEnabled());
                    break;
                case 1:
                    for (Component component : getMainFrame().getComponents()) {
                        if (component instanceof ModuleComponent && component != this) {
                            final ModuleComponent moduleComponent = (ModuleComponent) component;
                            if (moduleComponent.getModule().getCategory() == getModule().getCategory())
                            moduleComponent.setExtended(false);
                        }
                    }
                    setExtended(!isExtended());
                    break;
                default:
                    break;
            }
        }
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

    public IModule getModule() {
        return module;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }
}
