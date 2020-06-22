package best.reich.ingrosware.hud.manager;

import best.reich.ingrosware.manager.impl.AbstractMapManager;
import best.reich.ingrosware.util.ClassUtil;
import com.google.common.reflect.ClassPath;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import best.reich.ingrosware.hud.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class ComponentManager extends AbstractMapManager<String, Component> {
    private final File dir;

    public ComponentManager(File dir) {
        this.dir = dir;

        if(!dir.exists())
            dir.mkdirs();
    }

    @Override
    public void start() {
        loadInternalComponents();
        loadExternalComponents();

        getValues().forEach(Component::init);
        load();
    }

    @Override
    public void close() {
        save();
    }

    public void register(Component component) {
        put(component.getLabel(), component);
    }

    private void loadInternalComponents() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassPath classpath = ClassPath.from(loader);
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive("best.reich.ingrosware.hud.impl")) {
                final Class<?> componentClass = classInfo.load();
                if(Component.class.isAssignableFrom(componentClass)) {
                    final Component component = (Component) componentClass.newInstance();
                    register(component);

                    System.out.println(String.format("[Ingros] Registered Component %s", classInfo.getSimpleName()));
                }
            }

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void loadExternalComponents() {
        try {
            final File file = new File(dir, "externals");
            if(!file.exists())
                file.mkdirs();
            if (ClassUtil.getClassesEx(file.getPath()).isEmpty()) System.out.println("[Ingros] No external components found!");
            for (Class clazz : ClassUtil.getClassesEx(file.getPath())) {
                if (clazz != null && Component.class.isAssignableFrom(clazz)) {
                    final Component module = (Component) clazz.newInstance();
                    register(module);
                    System.out.println(String.format("[Ingros] Found External Component %s", module.getLabel()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        getValues().forEach(component -> {
            Path componentConfig = new File(dir, component.getLabel().toLowerCase() + ".json").toPath();
            if (Files.exists(componentConfig)) {
                try (Reader reader = new FileReader(componentConfig.toFile())) {
                    final JsonElement element = new JsonParser().parse(reader);
                    if (element.isJsonObject())
                        component.fromJson(element.getAsJsonObject());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void save() {
        File[] configurations = dir.listFiles();
        if (configurations != null) {
            for (File configuration : configurations)
                if(configuration.isFile())
                    configuration.delete();
        }

        getValues().forEach(component -> {
            Path componentConfig = new File(dir, component.getLabel().toLowerCase() + ".json").toPath();
            final JsonObject object = component.toJson();
            if (!object.entrySet().isEmpty()) {
                try {
                    Files.createFile(componentConfig);
                } catch (IOException e) {
                    return;
                }
                try (Writer writer = new FileWriter(componentConfig.toFile())) {
                    writer.write(new GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                            .toJson(object));
                } catch (IOException ignored) { }
            }
        });
    }

    public Component getComponent(String name) {
        return getRegistry().get(name);
    }
}
