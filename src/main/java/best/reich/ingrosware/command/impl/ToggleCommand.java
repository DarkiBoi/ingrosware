package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.util.other.chat.ChatColor;

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
