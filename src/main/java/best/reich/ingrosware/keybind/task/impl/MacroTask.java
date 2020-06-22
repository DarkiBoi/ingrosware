package best.reich.ingrosware.keybind.task.impl;

import net.minecraft.client.Minecraft;
import best.reich.ingrosware.keybind.task.Task;
import best.reich.ingrosware.macro.Macro;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class MacroTask implements Task {
    private final Macro macro;

    public MacroTask(Macro macro) {
        this.macro = macro;
    }

    @Override
    public void execute() {
        Minecraft.getMinecraft().player.sendChatMessage(macro.getCommand());
    }
}
