package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.util.other.chat.ChatColor;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/19/2020
 **/
@CommandManifest(label = "Macros", description = "add or remove a macro", handles = {"mac","macro"})
public class MacroCommand extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "list":
                    if (IngrosWare.INSTANCE.getMacroManager().getValues().isEmpty()) {
                        clientChatMsg().appendText("Your macro list is empty!", ChatColor.RED).send();
                        return;
                    }
                    clientChatMsg().appendText("Your macros are: ", new ChatColor[0]).send();
                    IngrosWare.INSTANCE.getMacroManager().getValues().forEach(macro ->
                            clientChatMsg().appendText("Label: " + macro.getLabel() + ", Keybind: "
                                    + Keyboard.getKeyName(macro.getKey()) + ", Command: " + macro.getCommand() + ".", new ChatColor[0]).send());
                    break;
                case "reload":
                    IngrosWare.INSTANCE.getMacroManager().clear();
                    IngrosWare.INSTANCE.getMacroManager().load();
                    clientChatMsg().appendText("Reloaded macros.", new ChatColor[0]).send();
                    break;
                case "remove":
                case "delete":
                    if (args.length < 3) {
                        clientChatMsg().appendText("Invalid args", ChatColor.RED).send();
                        return;
                    }
                    if (IngrosWare.INSTANCE.getMacroManager().isMacro(args[2])) {
                        IngrosWare.INSTANCE.getMacroManager().remove(args[2]);
                        clientChatMsg().appendText("Removed ", ChatColor.WHITE).appendText(args[2], ChatColor.WHITE).appendText(".", ChatColor.WHITE).send();
                        IngrosWare.INSTANCE.getMacroManager().close();
                    } else {
                        clientChatMsg().appendText(args[2], ChatColor.RED).appendText(" is not a macro.", ChatColor.RED).send();
                    }
                    break;
                case "clear":
                    if (IngrosWare.INSTANCE.getMacroManager().getValues().isEmpty()) {
                        clientChatMsg().appendText("Your macro list is empty!", ChatColor.RED).send();
                        return;
                    }
                    clientChatMsg().appendText("Cleared all macros", ChatColor.WHITE).send();
                    IngrosWare.INSTANCE.getMacroManager().clear();
                    IngrosWare.INSTANCE.getMacroManager().close();
                    break;
                case "add":
                case "create":
                    if (args.length < 5) {
                        clientChatMsg().appendText("Invalid args", ChatColor.RED).send();
                        return;
                    }
                    int keyCode = Keyboard.getKeyIndex(args[3].toUpperCase());
                    if (keyCode != -1 && !Keyboard.getKeyName(keyCode).equals("NONE")) {
                        if (IngrosWare.INSTANCE.getMacroManager().getMacroByKey(keyCode) != null) {
                            clientChatMsg().appendText("There is already a macro bound to that key.", ChatColor.RED).send();
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 4; i < args.length; i++) {
                            stringBuilder.append(args[i]);
                            if (i != args.length - 1) stringBuilder.append(" ");
                        }
                        IngrosWare.INSTANCE.getMacroManager().addMacro(args[2], keyCode, stringBuilder.toString());
                        clientChatMsg().appendText("Bound ", ChatColor.WHITE).
                                appendText(args[2], ChatColor.WHITE).appendText(" to ", ChatColor.WHITE).
                                appendText(Keyboard.getKeyName(keyCode), ChatColor.WHITE).send();
                        IngrosWare.INSTANCE.getMacroManager().close();
                    } else {
                        clientChatMsg().appendText("That is not a valid key code.", ChatColor.RED).send();
                    }
                    break;
            }
        } else {
            clientChatMsg().appendText("Not enough arguments!", ChatColor.RED).send();
        }
    }
}
