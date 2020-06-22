package best.reich.ingrosware.module.manager;

import best.reich.ingrosware.manager.impl.AbstractMapManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.ClassPath;
import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.util.ClassUtil;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ModuleManager extends AbstractMapManager<String, Module> {
    private final File dir;

    public ModuleManager(File dir) {
        this.dir = dir;

        if(!dir.exists())
            dir.mkdirs();
    }

    @Override
    public void start() {
        loadInternalModules();
        loadExternalModules();

        load();
    }

    @Override
    public void close() {
        save();
    }

    private void register(Module module) {
        put(module.getLabel(), module);
    }

    private void loadInternalModules() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive("best.reich.ingrosware.module.impl")) {
                final Class<?> moduleClass = classInfo.load();
                if(Module.class.isAssignableFrom(moduleClass)) {
                    final Module module = (Module) moduleClass.newInstance();
                    register(module);
                    System.out.println(String.format("[Ingros] Registered Module %s",classInfo.getSimpleName()));
                }
            }

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void loadExternalModules() {
        try {
            final File file = new File(dir, "externals");
            if(!file.exists())
                file.mkdirs();
            if (ClassUtil.getClassesEx(file.getPath()).isEmpty()) System.out.println("[Ingros] No external modules found!");
            for (Class clazz : ClassUtil.getClassesEx(file.getPath())) {
                if (clazz != null && ToggleableModule.class.isAssignableFrom(clazz)) {
                    final ToggleableModule module = (ToggleableModule) clazz.newInstance();
                    register(module);
                    System.out.println(String.format("[Ingros] Found External Module %s", module.getLabel()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        getValues().forEach(module -> {
            Path moduleConfig = new File(dir, module.getLabel().toLowerCase() + ".json").toPath();
            if (Files.exists(moduleConfig)) {
                try (Reader reader = new FileReader(moduleConfig.toFile())) {
                    final JsonElement element = new JsonParser().parse(reader);
                    if (element.isJsonObject())
                        module.fromJson(element.getAsJsonObject());
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

        getValues().forEach(module -> {
            Path moduleConfig = new File(dir, module.getLabel().toLowerCase() + ".json").toPath();
            final JsonObject object = module.toJson();
            if (!object.entrySet().isEmpty()) {
                try {
                    Files.createFile(moduleConfig);
                } catch (IOException e) {
                    return;
                }
                try (Writer writer = new FileWriter(moduleConfig.toFile())) {
                    writer.write(new GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                            .toJson(object));
                } catch (IOException ignored) { }
            }
        });
    }



    /**
     * could be done with a lambda/optional but they sloooow
     * @return
     */
    public List<ToggleableModule> getToggles() {
        final List<ToggleableModule> toggleableModules = new LinkedList<>();
        for(Module module : getValues()) {
            if(module instanceof ToggleableModule)
                toggleableModules.add((ToggleableModule) module);
        }

        return toggleableModules;
    }

    public Module getModule(String name) {
        return getRegistry().get(name);
    }

    public ToggleableModule getToggleByName(String name) {
        for(ToggleableModule toggleableModule : getToggles()) {
            if(toggleableModule.getLabel().equalsIgnoreCase(name)) return toggleableModule;
        }

        return null;
    }
}
