package us.devs.ingrosware.keybind.task.impl;

import us.devs.ingrosware.keybind.task.ITask;
import us.devs.ingrosware.module.types.ToggleableModule;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class ToggleModuleTask implements ITask {
    private final ToggleableModule toggleableModule;

    public ToggleModuleTask(ToggleableModule toggleableModule) {
        this.toggleableModule = toggleableModule;
    }

    @Override
    public void execute() {
        toggleableModule.toggle();
    }
}
