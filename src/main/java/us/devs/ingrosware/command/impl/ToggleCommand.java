package us.devs.ingrosware.command.impl;

import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.command.annotation.CommandManifest;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.other.chat.ChatColor;

@CommandManifest(label = "Toggle", description = "toggles a module", handles = {"t"})
public class ToggleCommand extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            clientChatMsg().appendText("Too little arguments!").send();
            return;
        }
        final ToggleableModule module = IngrosWare.INSTANCE.getModuleManager().getToggleByName(args[1]);
        if (module != null) {
            module.toggle();

            clientChatMsg().appendText(module.getLabel()).appendText(" has been ").appendText(module.getState() ? "enabled" : "disabled", module.getState() ? ChatColor.GREEN : ChatColor.RED).send();
        } else {
            clientChatMsg().appendText("This is not a module.").send();
        }
    }
}
