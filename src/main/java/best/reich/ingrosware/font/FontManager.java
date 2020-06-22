package best.reich.ingrosware.font;

import best.reich.ingrosware.manager.impl.AbstractListManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class FontManager extends AbstractListManager<MCFontRenderer> {
    private MCFontRenderer currentFont;
    private final File dir;
    private boolean customFont = true;

    public FontManager(File dir) {
        this.dir = dir;

        if(!dir.exists())
            dir.mkdirs();
    }

    @Override
    public void start() {
        add(new MCFontRenderer("Main", new Font("Tahoma", Font.PLAIN, 20), true, true));

        loadExternalFonts();
        setCurrentFont(get(0));
    }

    @Override
    public void close() {

    }

    private void loadExternalFonts() {
        try {
            for(File file : dir.listFiles()) {
                String name = file.getName();
                int lastIndexOf = name.lastIndexOf(".");
                if(name.substring(lastIndexOf).equalsIgnoreCase(".ttf")) {
                    add(new MCFontRenderer(name.substring(0, lastIndexOf),
                            fontFromTTF(file, 22, Font.PLAIN),
                            true, true));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private Font fontFromTTF(File fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, fontLocation);
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void setCurrentFont(MCFontRenderer currentFont) {
        this.currentFont = currentFont;
    }

    public boolean isUsingCustomFont() {
        return customFont;
    }

    public void setCustomFont(boolean customFont) {
        this.customFont = customFont;
    }

    public MCFontRenderer getCurrentFont() {
        return currentFont;
    }
}
