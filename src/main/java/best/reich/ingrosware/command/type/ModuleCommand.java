package best.reich.ingrosware.command.type;

import best.reich.ingrosware.command.annotation.CommandManifest;
import best.reich.ingrosware.setting.impl.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.module.Module;
import best.reich.ingrosware.util.math.MathUtil;
import best.reich.ingrosware.util.other.Logger;

import java.awt.*;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@CommandManifest(label = "")
public class ModuleCommand extends Command {
    private final Module module;

    public ModuleCommand(Module module) {
        this.module = module;
        setLabel(module.getLabel());
        setDescription("a module");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2) {
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(module).forEach(setting -> {
                if (args[1].equalsIgnoreCase(setting.getLabel())) {
                    if (setting instanceof BooleanSetting) {
                        final BooleanSetting booleansetting = (BooleanSetting) setting;
                        booleansetting.setValue(!booleansetting.getValue());
                        Logger.printMessage(booleansetting.getLabel() + " has been " + (booleansetting.getValue() ? ChatFormatting.PREFIX_CODE + "aenabled" + ChatFormatting.PREFIX_CODE + "7" : ChatFormatting.PREFIX_CODE + "cdisabled" + ChatFormatting.PREFIX_CODE + "7") + " for " + module.getLabel() + ".");
                    } else if (setting instanceof ModeStringSetting) {
                        final ModeStringSetting modeStringsetting = (ModeStringSetting) setting;
                        if (args.length >= 3) {
                            if (args[2].equalsIgnoreCase("help")) {
                                Logger.printMessage(modeStringsetting.getLabel() + "'s options are {");
                                for (String mode : modeStringsetting.getModes()) {
                                    Logger.printMessage(mode);
                                }
                                Logger.printMessage("}");
                            } else {
                                setting.setValue(args[2]);
                                Logger.printMessage(setting.getLabel() + " has been set to " + setting.getValue() + " for " + module.getLabel() + ".");
                            }
                        } else {
                            Logger.printMessage("Not enough arguments to change setting.");
                        }
                    } else if (setting instanceof ColorSetting) {
                        final ColorSetting colorSetting = (ColorSetting) setting;
                        if (args.length >= 5) {
                            try {
                                final int r = MathUtil.clamp(Integer.parseInt(args[2]), 0, 255);
                                final int g = MathUtil.clamp(Integer.parseInt(args[3]), 0, 255);
                                final int b = MathUtil.clamp(Integer.parseInt(args[4]), 0, 255);
                                if (args.length > 5) {
                                    final int a = MathUtil.clamp(Integer.parseInt(args[5]), 0, 255);
                                    colorSetting.setValue(new Color(r, g, b, a));
                                } else {
                                    colorSetting.setValue(new Color(r, g, b));
                                }
                                Logger.printMessage(setting.getLabel() + " has been set to " + colorSetting.getValue().getRGB() + " for " + module.getLabel() + ".");
                            } catch (Exception e) {
                                Logger.printMessage("Not enough arguments to change setting.");
                            }
                        } else {
                            Logger.printMessage("Not enough arguments to change setting.");
                        }
                    } else if (setting instanceof StringSetting) {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                stringBuilder.append(args[i]);
                                if (i != args.length - 1) stringBuilder.append(" ");
                            }
                            setting.setValue(stringBuilder.toString());
                            Logger.printMessage(setting.getLabel() + " has been set to " + setting.getValue() + " for " + module.getLabel() + ".");
                        }
                    } else if (setting instanceof BindSetting) {
                        final BindSetting bindSetting = (BindSetting) setting;
                        if (args.length >= 3) {
                            final int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
                            if (keyCode != -1) {
                                setting.setValue(keyCode);
                                Logger.printMessage(setting.getLabel() + " has been set to " + Keyboard.getKeyName(bindSetting.getValue()) + " for " + module.getLabel() + ".");
                            } else
                            Logger.printMessage("Invalid key code.");
                        } else {
                            Logger.printMessage("Not enough arguments to change setting.");
                        }
                    } else {
                        if (args.length >= 3) {
                            setting.setValue(args[2]);
                            Logger.printMessage(setting.getLabel() + " has been set to " + setting.getValue() + " for " + module.getLabel() + ".");
                        } else {
                            Logger.printMessage("Not enough arguments to change setting.");
                        }
                    }
                }
            });
        } else {
            StringBuilder builder = new StringBuilder(module.getLabel() + " " + ChatFormatting.PREFIX_CODE + "8[" + ChatFormatting.PREFIX_CODE + "7" + IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(module).size() + ChatFormatting.PREFIX_CODE + "8]" + ChatFormatting.PREFIX_CODE + "7: ");
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(module)
                    .forEach(setting -> builder.append(ChatFormatting.PREFIX_CODE).append(setting instanceof BooleanSetting ? (((BooleanSetting) setting).getValue() ? "a" : "c") : "7").append(setting.getLabel()).append(ChatFormatting.PREFIX_CODE + "8, "));
            Logger.printMessage(builder.toString().substring(0, builder.length() - 2));
        }
    }
}

