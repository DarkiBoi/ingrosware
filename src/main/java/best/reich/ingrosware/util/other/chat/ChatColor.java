package best.reich.ingrosware.util.other.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
public enum ChatColor {
    BLACK(TextFormatting.BLACK),
    DARK_BLUE(TextFormatting.DARK_BLUE),
    DARK_GREEN(TextFormatting.DARK_GREEN),
    DARK_AQUA(TextFormatting.DARK_AQUA),
    DARK_RED(TextFormatting.DARK_RED),
    DARK_PURPLE(TextFormatting.DARK_PURPLE),
    GOLD(TextFormatting.GOLD),
    GRAY(TextFormatting.GRAY),
    DARK_GRAY(TextFormatting.DARK_GRAY),
    BLUE(TextFormatting.BLUE),
    GREEN(TextFormatting.GREEN),
    AQUA(TextFormatting.AQUA),
    RED(TextFormatting.RED),
    LIGHT_PURPLE(TextFormatting.LIGHT_PURPLE),
    YELLOW(TextFormatting.YELLOW),
    WHITE(TextFormatting.WHITE),
    OBFUSCATED(TextFormatting.OBFUSCATED),
    BOLD(TextFormatting.BOLD),
    STRIKETHROUGH(TextFormatting.STRIKETHROUGH),
    UNDERLINE(TextFormatting.UNDERLINE),
    ITALIC(TextFormatting.ITALIC),
    RESET(TextFormatting.RESET);

    private TextFormatting baseColor;

    private ChatColor(final TextFormatting baseColor) {
        this.baseColor = baseColor;
    }

    public TextFormatting getBaseColor() {
        return this.baseColor;
    }


}
