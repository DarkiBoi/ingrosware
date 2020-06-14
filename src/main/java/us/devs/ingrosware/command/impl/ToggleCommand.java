package us.devs.ingrosware.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.command.annotation.CommandManifest;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.other.Logger;

@CommandManifest(label = "Toggle", description = "toggles a module", handles = {"t"})
public class ToggleCommand extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            Logger.printMessage("Too little arguments!");
            return;
        }
        final ToggleableModule module = IngrosWare.INSTANCE.getModuleManager().getToggleByName(args[1]);
        if (module != null) {
            module.toggle();
            Logger.printMessage(module.getLabel() + " has been " + ChatFormatting.PREFIX_CODE + (module.isEnabled() ? "aenabled" : "cdisabled"));
        } else {
            Logger.printMessage("Not a mod nigga");
        }
    }
}
