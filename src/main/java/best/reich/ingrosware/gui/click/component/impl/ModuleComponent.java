package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.AbstractSetting;
import best.reich.ingrosware.setting.impl.*;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.gui.click.component.Component;
import best.reich.ingrosware.gui.click.frame.impl.MainFrame;

import java.util.ArrayList;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
public class ModuleComponent extends Component {
    private final MainFrame mainFrame;
    private final Module module;
    private boolean extended;
    private final ArrayList<Component> components = new ArrayList<>();
    private float scrollY;

    public ModuleComponent(MainFrame mainFrame, Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.mainFrame = mainFrame;
        this.module = module;
    }

    @Override
    public void moved(float newPosX, float newPosY) {
        super.moved(newPosX, newPosY);
        getComponents().forEach(component -> component.moved(newPosX, newPosY));
    }

    @Override
    public void initComponent() {
        super.initComponent();
        float offsetY = 42;
        if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(getModule()) != null) {
            for (AbstractSetting setting : IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(getModule())) {
                if (setting instanceof BooleanSetting) {
                    getComponents().add(new BooleanComponent((BooleanSetting) setting, getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
                    offsetY += 16;
                }
                if (setting instanceof NumberSetting) {
                    getComponents().add(new NumberComponent((NumberSetting) setting, getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
                    offsetY += 16;
                }
                if (setting instanceof ModeStringSetting) {
                    getComponents().add(new ModeStringComponent((ModeStringSetting) setting, getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
                    offsetY += 16;
                }
                if (setting instanceof ColorSetting) {
                    getComponents().add(new ColorComponent((ColorSetting) setting, getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
                    offsetY += 16;
                }
                if (setting instanceof BindSetting) {
                    getComponents().add(new BindComponent((BindSetting) setting, getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
                    offsetY += 16;
                }
            }
        }
        if (getModule() instanceof ToggleableModule) getComponents().add(new ModuleBindComponent((ToggleableModule) getModule(),getMainFrame().getPosX(), getMainFrame().getPosY(), 112, offsetY, 280, 14));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        updatePositions();
        if (MouseUtil.mouseWithin(mouseX, mouseY, getMainFrame().getPosX() + 110, getMainFrame().getPosY() + 40, 284, getMainFrame().getHeight() - 50) && getCurrentHeight() > (getMainFrame().getHeight() - 50)) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (getScrollY() - 6 < -(getCurrentHeight() - Math.min(getCurrentHeight(), (getMainFrame().getHeight() - 50))))
                    setScrollY((int) -(getCurrentHeight() - Math.min(getCurrentHeight(), (getMainFrame().getHeight() - 50))));
                else setScrollY(getScrollY() - 6);
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 6);
            }
        }
        if (getScrollY() > 0) setScrollY(0);
        if (getCurrentHeight() > (getMainFrame().getHeight() - 50)) {
            if (getScrollY() - 6 < -(getCurrentHeight() - (getMainFrame().getHeight() - 50)))
                setScrollY((int) -(getCurrentHeight() - (getMainFrame().getHeight() - 50)));
        } else if (getScrollY() < 0) setScrollY(0);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), getWidth(), getHeight(), 0.5f, getModule().isEnabled() ? 0xff505050 : 0xff353535, 0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(), getPosX() + getWidth() / 2 - (RenderUtil.getStringWidth(getLabel()) / 2), getPosY() + getHeight() - 1 - RenderUtil.getStringHeight(), getModule().isEnabled() ? -1 : 0xff858585);
        if (isExtended() && getMainFrame().getSelectedCatergory() == getModule().getCategory()) {
            for (Component component : getComponents()) {
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (isExtended() && getMainFrame().getSelectedCatergory() == getModule().getCategory()) {
            for (Component component : getComponents()) {
                component.keyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        if (hovered) {
            switch (mouseButton) {
                case 0:
                    if (getModule() instanceof ToggleableModule)
                        ((ToggleableModule) getModule()).toggle();
                    break;
                case 1:
                    if (!getComponents().isEmpty()) {
                        for (Component component : getMainFrame().getComponents()) {
                            if (component instanceof ModuleComponent && component != this) {
                                final ModuleComponent moduleComponent = (ModuleComponent) component;
                                if (moduleComponent.getModule().getCategory() == getModule().getCategory())
                                    moduleComponent.setExtended(false);
                            }
                        }
                        setExtended(!isExtended());
                    }
                    break;
                default:
                    break;
            }
        }
        if (isExtended() && getMainFrame().getSelectedCatergory() == getModule().getCategory()) {
            for (Component component : getComponents()) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (isExtended() && getMainFrame().getSelectedCatergory() == getModule().getCategory()) {
            for (Component component : getComponents()) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (isExtended() && getMainFrame().getSelectedCatergory() == getModule().getCategory()) {
            for (Component component : getComponents()) {
                component.onGuiClosed();
            }
        }
    }

    private void updatePositions() {
        float offsetY = 42;
        for (Component component : getComponents()) {
            component.setOffsetY(offsetY);
            component.moved(getMainFrame().getPosX(), getMainFrame().getPosY() + getScrollY());
            offsetY += component.getHeight();
        }
    }

    private float getCurrentHeight() {
        float cHeight = 0;
        for (Component component : getComponents()) {
            cHeight += component.getHeight();
        }
        return cHeight;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public Module getModule() {
        return module;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }
}
