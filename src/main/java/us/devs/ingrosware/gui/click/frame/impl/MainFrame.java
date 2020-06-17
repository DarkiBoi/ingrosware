package us.devs.ingrosware.gui.click.frame.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.gui.click.component.Component;
import us.devs.ingrosware.gui.click.component.impl.CategoryComponent;
import us.devs.ingrosware.gui.click.component.impl.ModuleComponent;
import us.devs.ingrosware.gui.click.frame.Frame;
import us.devs.ingrosware.module.IModule;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.util.math.MouseUtil;
import us.devs.ingrosware.util.render.RenderUtil;

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
            getComponents().add(new CategoryComponent(this,moduleCategory,getPosX(),getPosY(),offsetX,20.0f,shift - 2,14));
            offsetX += shift + 0.5f;
            float offsetY = 42;
            for (IModule module : IngrosWare.INSTANCE.getModuleManager().getValues()) {
                if (module.getCategory() == moduleCategory) {
                    getComponents().add(new ModuleComponent(this,module,getPosX(),getPosY(),8,offsetY,96,14));
                    offsetY += 16;
                }
            }
        }
        getComponents().forEach(Component::initComponent);
    }

    @Override
    public void moved(float newPosX, float newPosY) {
        super.moved(newPosX, newPosY);
        getComponents().forEach(component -> component.moved(getPosX(),getPosY()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (isDragging()) moved(mouseX + getLastPosX(),mouseY + getLastPosY());
        if (getPosX() < 0) moved(0,getPosY());
        if (getPosY() < 0) moved(getPosX(),0);
        if (getPosX() + getWidth() > scaledResolution.getScaledWidth()) moved(scaledResolution.getScaledWidth() - getWidth(),getPosY());
        if (getPosY() + getHeight() > scaledResolution.getScaledHeight()) moved(getPosX(),scaledResolution.getScaledHeight() - getHeight());
        RenderUtil.drawBorderedRect(getPosX(),getPosY(),getWidth(),getHeight(),0.5f,0xff070707,0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 1.0f,getPosY() + 1.0f,getWidth() - 2.0f,getHeight() - 2.0f,0.5f,0xff191919,0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 1.0f,getPosY() + 1.0f,getWidth() - 2.0f,14,0.5f,0xff070707,0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 4.0f,getPosY() + 18,getWidth() - 8.0f,18,0.5f,0xff252525,0xff000000);
        RenderUtil.drawBorderedRect(getPosX() + 6.0f,getPosY() + 40,100,getHeight() - 40,0.5f,0xff252525,0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(),getPosX() + 4,getPosY() + 4,-1);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                component.drawScreen(mouseX,mouseY,partialTicks);
            }
            if (component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == getSelectedCatergory()) {
                component.drawScreen(mouseX,mouseY,partialTicks);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                component.keyTyped(typedChar,keyCode);
            }
            if (component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == getSelectedCatergory()) {
                component.keyTyped(typedChar,keyCode);
            }
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
            if (component instanceof CategoryComponent) {
                component.mouseClicked(mouseX,mouseY,mouseButton);
            }
            if (component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == getSelectedCatergory()) {
                component.mouseClicked(mouseX,mouseY,mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && isDragging()) setDragging(false);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                component.mouseReleased(mouseX,mouseY,mouseButton);
            }
            if (component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == getSelectedCatergory()) {
                component.mouseReleased(mouseX,mouseY,mouseButton);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                component.onGuiClosed();
            }
            if (component instanceof ModuleComponent && ((ModuleComponent)component).getModule().getCategory() == getSelectedCatergory()) {
                component.onGuiClosed();
            }
        }
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
