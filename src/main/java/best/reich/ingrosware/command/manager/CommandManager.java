package best.reich.ingrosware.command.manager;

import best.reich.ingrosware.manager.impl.AbstractMapManager;
import com.google.common.reflect.ClassPath;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.command.type.HudComponentCommand;
import best.reich.ingrosware.command.type.ModuleCommand;
import best.reich.ingrosware.util.ClassUtil;

import java.io.File;
import java.io.IOException;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class CommandManager extends AbstractMapManager<String, Command> {
    private final File dir;

    public CommandManager(File dir) {
        this.dir = dir;

        if(!dir.exists())
            dir.mkdirs();
    }

    @Override
    public void start() {
        loadInternalCommands();
        loadExternalCommands();

        IngrosWare.INSTANCE.getModuleManager().getValues().forEach(module -> {
            if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(module) != null)
                register(new ModuleCommand(module));
        });
        IngrosWare.INSTANCE.getComponentManager().getValues().forEach(component -> {
            if (IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(component) != null)
                register(new HudComponentCommand(component));
        });
    }

    @Override
    public void close() {
    }

    private void register(Command command) {
        put(command.getLabel(), command);
    }

    private void loadInternalCommands() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive("best.reich.ingrosware.command.impl")) {
                final Class<?> commandClass = classInfo.load();
                if(Command.class.isAssignableFrom(commandClass)) {
                    final Command command = (Command) commandClass.newInstance();
                    register(command);
                    System.out.println(String.format("[Ingros] Registered Command %s", classInfo.getSimpleName()));
                }
            }

        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void loadExternalCommands() {
        try {
            final File file = new File(dir, "externals");
            if (!file.exists()) {
                file.mkdirs();
            }
            if (ClassUtil.getClassesEx(file.getPath()).isEmpty()) System.out.println("[Ingros] No external commands found!");
            for (Class clazz : ClassUtil.getClassesEx(file.getPath())) {
                if (clazz != null) {
                    if (Command.class.isAssignableFrom(clazz)) {
                        final Command command = (Command) clazz.newInstance();
                        if (command != null) {
                            register(command);
                            System.out.println(String.format("[Ingros] Found External Command %s", command.getLabel()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
