package best.reich.ingrosware.gui.font;

import best.reich.ingrosware.font.MCFontRenderer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import best.reich.ingrosware.IngrosWare;

import java.io.IOException;
import java.util.Map;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 *
 * legit gui language reworked lol.
 **/
public class GuiFontSelector extends GuiScreen {
    protected GuiScreen parentScreen;
    private GuiFontSelector.List list;
    private GuiOptionButton guiOptionButton;

    public GuiFontSelector(GuiScreen screen) {
        this.parentScreen = screen;
    }

    @Override
    public void initGui() {
        this.guiOptionButton = addButton(new GuiOptionButton(1,
                this.width / 2 - 155, this.height - 38,
                "Use Custom Font?: " +
                        IngrosWare.INSTANCE.getFontManager().isUsingCustomFont()));
        this.buttonList.add(new GuiOptionButton(5,
                this.width / 2 - 155 + 160, this.height - 38, "Done"));
        this.list = new GuiFontSelector.List(this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 5:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                case 1:
                    IngrosWare.INSTANCE.getFontManager().setCustomFont(!IngrosWare.INSTANCE.getFontManager().isUsingCustomFont());
                    button.displayString = "Use Custom Font?: " + IngrosWare.INSTANCE.getFontManager().isUsingCustomFont();
                    break;
                default:
                    this.list.actionPerformed(button);
                    break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "Font Selector", this.width / 2, 16, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    class List extends GuiSlot {
        private final java.util.List<String> codeList = Lists.newArrayList();
        private final Map<String, MCFontRenderer> fontMap = Maps.newHashMap();

        public List(Minecraft mcIn) {
            super(mcIn, GuiFontSelector.this.width, GuiFontSelector.this.height, 32,
                    GuiFontSelector.this.height - 65 + 4, 18);

            for (MCFontRenderer font : IngrosWare.INSTANCE.getFontManager().getList()) {
                this.fontMap.put(font.getLabel(), font);
                this.codeList.add(font.getLabel());
            }
        }

        protected int getSize() {
            return this.codeList.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            MCFontRenderer font = fontMap.get(codeList.get(slotIndex));
            IngrosWare.INSTANCE.getFontManager().setCurrentFont(font);
        }

        protected boolean isSelected(int slotIndex) {
            return (this.codeList.get(slotIndex)).equals(IngrosWare.INSTANCE.getFontManager().getCurrentFont().getLabel());
        }

        protected int getContentHeight() {
            return this.getSize() * 18;
        }

        protected void drawBackground() {
            GuiFontSelector.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5, float v) {
            GuiFontSelector.this.
                    drawCenteredString(GuiFontSelector.this.fontRenderer,
                            (this.fontMap.get(this.codeList.get(i))).getLabel(),
                            this.width / 2, i2 + 1, 16777215);
        }
    }

}
