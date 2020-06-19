package us.devs.ingrosware.macro;

import com.google.gson.JsonObject;
import us.devs.ingrosware.keybind.Keybind;
import us.devs.ingrosware.keybind.task.impl.MacroTask;
import us.devs.ingrosware.traits.Configable;
import us.devs.ingrosware.traits.Labelable;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class Macro implements Labelable, Configable {
    private String label, command;
    private Keybind keybind;

    public Macro(String label, int key, String command) {
        this.label = label;
        this.command = command;
        this.keybind = new Keybind(key, new MacroTask(this));
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getCommand() {
        return command;
    }

    public int getKey() {
        return keybind.getKey();
    }

    public void setKey(int key) {
        this.keybind.setKey(key);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCommand(String command) {
        this.command = command;
    }


    public Keybind getKeybind() {
        return keybind;
    }

    @Override
    public void save(JsonObject destination) {
        destination.addProperty("Label", getLabel());
        destination.addProperty("Bind", getKey());
        destination.addProperty("Command", getCommand());
    }

    @Override
    public void load(JsonObject source) {
        if (source.has("Label")) {
            setLabel(source.get("Label").getAsString());
        }
        if (source.has("Bind")) {
            setKey(source.get("Bind").getAsInt());
        }
        if (source.has("Command") ) {
            setCommand(source.get("Command").getAsString());
        }
    }
}
