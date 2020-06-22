package best.reich.ingrosware.gui.hud;

import best.reich.ingrosware.hud.type.CustomComponent;
import best.reich.ingrosware.util.render.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.IngrosWare;

import java.io.IOException;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class CustomHudComponent extends GuiScreen {
    private GuiTextField text;

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100,
                height / 4 + 92 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100,
                height / 4 + 116 + 12, "Back"));
        this.text = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.text.setMaxStringLength(Integer.MAX_VALUE);
        this.text.setFocused(true);

    }

    @Override
    public void keyTyped(char character, int keyCode) throws IOException {
        this.text.textboxKeyTyped(character, keyCode);
        if (keyCode == Keyboard.KEY_TAB) {
            this.text.setFocused(!this.text.isFocused());
        }
        if (keyCode == Keyboard.KEY_RETURN) {
            actionPerformed(this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        text.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawString(mc.fontRenderer, "Custom Text Component", (int) (width / 2 - RenderUtil.getStringWidth("Custom Text Component") / 2), 20,
                0xFFFFFFFF);
        if (text.getText().isEmpty()) {
            drawString(mc.fontRenderer, "I hate zim", (int) (width / 2 - RenderUtil.getStringWidth("I hate zim") / 2),
                    66, 0xFF888888);
        }
        text.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if(!text.getText().isEmpty()) {
                    final CustomComponent customComponent = new CustomComponent(text.getText());
                    IngrosWare.INSTANCE.getComponentManager().register(customComponent);
                    mc.displayGuiScreen(new GuiHudEditor());
                }
                break;
            case 1:
                mc.displayGuiScreen(new GuiHudEditor());
                break;
        }
    }
}
