package best.reich.ingrosware.macro;

import best.reich.ingrosware.keybind.Keybind;
import best.reich.ingrosware.keybind.task.impl.MacroTask;
import best.reich.ingrosware.traits.Configable;
import best.reich.ingrosware.traits.Labelable;
import com.google.gson.JsonObject;

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
    public JsonObject toJson() {
        final JsonObject object = new JsonObject();
        object.addProperty("Label", getLabel());
        object.addProperty("Bind", getKey());
        object.addProperty("Command", getCommand());

        return object;
    }

    @Override
    public void fromJson(JsonObject source) {
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
