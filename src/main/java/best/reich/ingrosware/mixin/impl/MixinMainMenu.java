package best.reich.ingrosware.mixin.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.gui.font.GuiFontSelector;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Mixin(GuiMainMenu.class)
public class MixinMainMenu extends GuiScreen {

    private int fontSelectorButton = 1337;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void setup(CallbackInfo ci) {
        for (GuiButton button : this.buttonList) {
            if (button.id == fontSelectorButton)
                fontSelectorButton++;
        }

        this.buttonList.add(new GuiButton(fontSelectorButton, 5, 15,
                fontRenderer.getStringWidth("Font Selector") + 5, 20, "Font Selector"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void onClick(GuiButton button, CallbackInfo ci) {
        if (button.id == fontSelectorButton) {
            mc.displayGuiScreen(new GuiFontSelector(this));
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(IngrosWare.INSTANCE.getLabel(), 2, 2, -1);
    }

}
