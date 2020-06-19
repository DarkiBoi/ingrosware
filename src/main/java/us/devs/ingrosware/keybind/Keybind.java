package us.devs.ingrosware.keybind;

import us.devs.ingrosware.keybind.task.ITask;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class Keybind {
    private int key;
    private final ITask task;

    public Keybind(int key, ITask task) {
        this.key = key;
        this.task = task;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ITask getTask() {
        return task;
    }
}
