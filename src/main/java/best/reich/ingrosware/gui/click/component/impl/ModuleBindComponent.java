package best.reich.ingrosware.gui.click.component.impl;

import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.util.math.MouseUtil;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.gui.click.component.Component;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/18/2020
 **/
public class ModuleBindComponent extends Component {
    private final ToggleableModule toggleableModule;
    private boolean binding;
    public ModuleBindComponent(ToggleableModule toggleableModule, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super("Bind", posX, posY, offsetX, offsetY, width, height);
        this.toggleableModule = toggleableModule;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.drawBorderedRect(getPosX(),getPosY(),80,getHeight(),0.5f,0xff353535,0xff000000);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(isBinding() ? "...":Keyboard.getKeyName(getToggleableModule().getBind()),getPosX() + 2,getPosY() + getHeight() -1- RenderUtil.getStringHeight(),-1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getLabel(),getPosX() + 82,getPosY() + getHeight() -1- RenderUtil.getStringHeight(),-1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && MouseUtil.mouseWithin(mouseX,mouseY,getPosX(),getPosY(),80,getHeight())) setBinding(!isBinding());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (isBinding()) {
            getToggleableModule().setBind(keyCode);
            setBinding(false);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        setBinding(false);
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public ToggleableModule getToggleableModule() {
        return toggleableModule;
    }
}
