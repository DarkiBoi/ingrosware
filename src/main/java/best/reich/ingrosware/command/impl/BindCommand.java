package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.util.other.chat.ChatColor;

@CommandManifest(label = "Keybind", description = "set a module keybind!", handles = {"bind","key"})
public class BindCommand extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 3) {
            final String moduleName = args[1];
            final ToggleableModule module = IngrosWare.INSTANCE.getModuleManager().getToggleByName(moduleName);
            if (module != null) {
                final int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
                if (keyCode != -1) {
                    module.setBind(keyCode);
                    clientChatMsg().appendText(module.getLabel(), ChatColor.WHITE)
                            .appendText(" is now bound to ", ChatColor.WHITE).appendText(Keyboard.getKeyName(keyCode), ChatColor.WHITE).send();
                } else {
                    clientChatMsg().appendText("This is not a valid key code.", ChatColor.RED).send();
                }
            } else {
                clientChatMsg().appendText("That module does not exist!", ChatColor.RED).send();
            }
        } else {
            clientChatMsg().appendText("Invalid arguments!", ChatColor.RED).appendText(" Usage: -bind [module] [key]", ChatColor.RED).send();
        }
    }
}
