package best.reich.ingrosware.keybind;

import best.reich.ingrosware.keybind.task.Task;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class Keybind {
    private int key;
    private final Task task;

    public Keybind(int key, Task task) {
        this.key = key;
        this.task = task;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Task getTask() {
        return task;
    }
}
