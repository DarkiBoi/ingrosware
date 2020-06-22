package best.reich.ingrosware.macro;

import best.reich.ingrosware.manager.impl.AbstractMapManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
public class MacroManager extends AbstractMapManager<String, Macro> {
    private final File dir;

    public MacroManager(File dir) {
        this.dir = dir;

        if(!dir.exists())
            dir.mkdirs();
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void close() {
        save();
    }

    public void addMacro(String label, int key,String text) {
        put(label.toLowerCase(), new Macro(label, key, text));
    }

    public Macro getMacro(String label) {
        return getRegistry().get(label.toLowerCase());
    }

    public boolean isMacro(String label) {
        return getMacro(label) != null;
    }

    public Macro getMacroByKey(int key) {
        for (Macro macro : getValues()) {
            if (macro.getKey() == key) {
                return macro;
            }
        }
        return null;
    }
    
    public void load() {
        getValues().forEach(macro -> {
            Path macroConfig = new File(dir, macro.getLabel().toLowerCase() + ".json").toPath();
            if (Files.exists(macroConfig)) {
                try (Reader reader = new FileReader(macroConfig.toFile())) {
                    final JsonElement element = new JsonParser().parse(reader);
                    if (element.isJsonObject())
                        macro.fromJson(element.getAsJsonObject());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void save() {
        File[] configurations = dir.listFiles();
        if (configurations != null) {
            for (File configuration : configurations)
                if(configuration.isFile())
                    configuration.delete();
        }

        getValues().forEach(macro -> {
            Path macroConfig = new File(dir, macro.getLabel().toLowerCase() + ".json").toPath();
            final JsonObject object = macro.toJson();
            if (!object.entrySet().isEmpty()) {
                try {
                    Files.createFile(macroConfig);
                } catch (IOException e) {
                    return;
                }
                try (Writer writer = new FileWriter(macroConfig.toFile())) {
                    writer.write(new GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                            .toJson(object));
                } catch (IOException ignored) { }
            }
        });
    }
}
