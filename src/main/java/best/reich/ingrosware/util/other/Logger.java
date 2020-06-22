package best.reich.ingrosware.util.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class Logger {

    public static void printComponent(TextComponentString textComponent) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
    }

    public static void printMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GRAY + "Ingros" + ChatFormatting.DARK_GRAY + " | " + ChatFormatting.WHITE + message));
    }
}
