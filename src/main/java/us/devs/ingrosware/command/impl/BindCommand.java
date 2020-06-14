package us.devs.ingrosware.command.impl;

import org.lwjgl.input.Keyboard;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.command.annotation.CommandManifest;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.other.Logger;

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
                    Logger.printMessage(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".");
                } else {
                    Logger.printMessage("That is not a valid key code.");
                }
            } else {
                Logger.printMessage("That module does not exist.");
                Logger.printMessage("Type \"modules\" for a list of all modules.");
            }
        } else {
            Logger.printMessage("Invalid arguments.");
            Logger.printMessage("Usage: \"bind [module] [key]\"");
        }
    }
}
