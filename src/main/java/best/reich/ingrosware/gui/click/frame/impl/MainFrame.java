package best.reich.ingrosware.gui.click.frame.impl;

import best.reich.ingrosware.gui.click.component.Component;
import best.reich.ingrosware.gui.click.component.impl.CategoryComponent;
import best.reich.ingrosware.gui.click.component.impl.ModuleComponent;
import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.gui.click.frame.Frame;

import java.util.ArrayList;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/17/2020
 **/
public class MainFrame extends Frame {
    private ModuleCategory selectedCatergory = ModuleCategory.COMBAT;
    private final ArrayList<Component> components = new ArrayList<>();

    public MainFrame(String label, float posX, float posY, float width, float height) {
        super(label, posX, posY, width, height);
    }

    @Override
    public void initFrame() {
        super.initFrame();
        float offsetX = 6;
        final float shift = (getWidth() - 12) / ModuleCategory.values().length;
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            getComponents().add(new CategoryComponent(this, moduleCategory, getPosX(), getPosY(), offsetX, 20.0f, shift - 2, 14));
            offsetX += shift + 0.5f;
            float offsetY = 42;
            for (Module module : IngrosWare.INSTANCE.getModuleManager().getValues()) {
                if (module.getCategory() == moduleCategory) {
                    getComponents().add(new ModuleComponent(this, module, getPosX(), getPosY(), 8, offsetY, 96, 14));
                    offsetY += 16;
                }
            }
        }
        getComponents().forEach(Component::initComponent);
    }

    @Override
    public void moved(float newPosX, float newPosY) {
        super.moved(newPosX, newPosY);
        getComponents().forEach(component -> component.moved(getPosX(), getPosY()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (isDragging()) moved(mouseX + getLastPosX(), mouseY + getLastPosY());
        if (getPosX() < 0) moved(0, getPosY());
        if (getPosY() < 0) moved(getPosX(), 0);
        if (getPosX() + getWidth() > scaledResolution.getScaledWidth())
            moved(scaledResolution.getScaledWidth() - getWidth(), getPosY());
        if (getPosY() + getHeight() > scaledResolution.getScaledHeight())
            moved(getPosX(), scaledResolution.getScaledHeight() - getHeight());
        updatePositions();
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getModuleCategory() == getSelectedCatergory()) {
                    if (MouseUtil.mouseWithin(mouseX, mouseY, getPosX() + 6.0f, getPosY() + 40, 100, getHeight() - 50) && getCurrentHeight() > (getHeight() - 50)) {
                        int wheel = Mouse.getDWheel();
                        if (wheel < 0) {
                            if (getScrollY() - 6 < -(getCurrentHeight() - Math.min(getCurrentHeight(), (getHeight() - 50))))
                                categoryComponent.setScrollY((int) -(getCurrentHeight() - Math.min(getCurrentHeight(), (getHeight() - 50))));
                            else categoryComponent.setScrollY(getScrollY() - 6);
                        } else if (wheel > 0) {
                            categoryComponent.setScrollY(getScrollY() + 6);
                        }
                    }
                    if (getScrollY() > 0) categoryComponent.setScrollY(0);
                    if (getCurrentHeight() > (getHeight() - 50)) {
                        if (getScrollY() - 6 < -(getCurrentHeight() - (getHeight() - 50)))
                            categoryComponent.setScrollY((int) -(getCurrentHeight() - (getHeight() - 50)));
                    } else if (getScrollY() < 0) categoryComponent.setScrollY(0);
                }
            }
        }
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), getWidth(), getHeight(), 0.5f, 0xff070707, 0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 1.0f, getPosY() + 1.0f, getWidth() - 2.0f, getHeight() - 2.0f, 0.5f, 0xff191919, 0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 1.0f, getPosY() + 1.0f, getWidth() - 2.0f, 14, 0.5f, 0xff070707, 0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 4.0f, getPosY() + 18, getWidth() - 8.0f, 18, 0.5f, 0xff252525, 0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 6.0f, getPosY() + 40, 100, getHeight() - 46, 0.5f, 0xff252525, 0xff000000);
        if (isExtended())
            RenderUtil.drawBorderedRect(getPosX() + 110, getPosY() + 40, 284, getHeight() - 46, 0.5f, 0xff252525, 0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(), getPosX() + 4, getPosY() + 4, -1);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent)
                component.drawScreen(mouseX, mouseY, partialTicks);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.prepareScissorBox(scaledResolution, getPosX() + 6.0f, getPosY() + 40, 388, getHeight() - 46);
            if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                component.drawScreen(mouseX, mouseY, partialTicks);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent)
                component.keyTyped(typedChar, keyCode);

            if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                component.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hovered = MouseUtil.mouseWithin(mouseX, mouseY, getPosX(), getPosY(), getWidth(), 14);
        if (mouseButton == 0 && hovered) {
            setDragging(true);
            setLastPosX(getPosX() - mouseX);
            setLastPosY(getPosY() - mouseY);
        }
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent)
                component.mouseClicked(mouseX, mouseY, mouseButton);
            if (MouseUtil.mouseWithin(mouseX, mouseY, getPosX() + 6.0f, getPosY() + 40, 388, getHeight() - 46)) {
                if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                    component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && isDragging()) setDragging(false);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent)
                component.mouseReleased(mouseX, mouseY, mouseButton);
            if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent)
                component.onGuiClosed();
            if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                component.onGuiClosed();
        }
    }

    private boolean isExtended() {
        for (Component component : getComponents()) {
            if (component instanceof ModuleComponent && ((ModuleComponent) component).isExtended() && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory())
                return true;
        }
        return false;
    }

    public float getScrollY() {
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getModuleCategory() == getSelectedCatergory()) {
                    return categoryComponent.getScrollY();
                }
            }
        }
        return 0;
    }

    private void updatePositions() {
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            float offsetY = 42;
            for (Component component : getComponents()) {
                if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == moduleCategory) {
                    component.setOffsetY(offsetY);
                    component.moved(getPosX(), getPosY() + getScrollY());
                    offsetY += component.getHeight();
                }
            }
        }
    }

    private float getCurrentHeight() {
        float cHeight = 0;
        for (Component component : getComponents()) {
            if (component instanceof ModuleComponent && ((ModuleComponent) component).getModule().getCategory() == getSelectedCatergory()) {
                cHeight += component.getHeight();
            }
        }
        return cHeight;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public ModuleCategory getSelectedCatergory() {
        return selectedCatergory;
    }

    public void setSelectedCatergory(ModuleCategory selectedCatergory) {
        this.selectedCatergory = selectedCatergory;
    }
}
