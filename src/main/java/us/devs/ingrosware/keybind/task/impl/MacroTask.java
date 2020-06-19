package us.devs.ingrosware.keybind.task.impl;

import net.minecraft.client.Minecraft;
import us.devs.ingrosware.keybind.task.ITask;
import us.devs.ingrosware.macro.Macro;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class MacroTask implements ITask {
    private final Macro macro;

    public MacroTask(Macro macro) {
        this.macro = macro;
    }

    @Override
    public void execute() {
        Minecraft.getMinecraft().player.sendChatMessage(macro.getCommand());
    }
}
