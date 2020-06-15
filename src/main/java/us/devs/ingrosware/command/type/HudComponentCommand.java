package us.devs.ingrosware.command.type;

import com.mojang.realmsclient.gui.ChatFormatting;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.command.annotation.CommandManifest;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.module.IModule;
import us.devs.ingrosware.setting.impl.BooleanSetting;
import us.devs.ingrosware.setting.impl.ColorSetting;
import us.devs.ingrosware.setting.impl.ModeStringSetting;
import us.devs.ingrosware.setting.impl.StringSetting;
import us.devs.ingrosware.util.math.MathUtil;
import us.devs.ingrosware.util.other.Logger;

import java.awt.*;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
@CommandManifest(label = "")
public class HudComponentCommand extends Command {
    private final Component hudComponent;

    public HudComponentCommand(Component hudComponent) {
        this.hudComponent = hudComponent;
        setLabel(hudComponent.getLabel());
        setDescription("a hud component");
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2) {
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(hudComponent).forEach(property -> {
                if (args[1].equalsIgnoreCase(property.getLabel())) {
                    if (property instanceof BooleanSetting) {
                        final BooleanSetting booleanProperty = (BooleanSetting) property;
                        booleanProperty.setValue(!booleanProperty.getValue());
                        Logger.printMessage(booleanProperty.getLabel() + " has been " + (booleanProperty.getValue() ? ChatFormatting.PREFIX_CODE + "aenabled" + ChatFormatting.PREFIX_CODE + "7" : ChatFormatting.PREFIX_CODE + "cdisabled" + ChatFormatting.PREFIX_CODE + "7") + " for " + hudComponent.getLabel() + ".");
                    } else if (property instanceof ModeStringSetting) {
                        final ModeStringSetting modeStringProperty = (ModeStringSetting) property;
                        if (args.length >= 3) {
                            if (args[2].equalsIgnoreCase("help")) {
                                Logger.printMessage(modeStringProperty.getLabel() + "'s options are {");
                                for (String mode : modeStringProperty.getModes()) {
                                    Logger.printMessage(mode);
                                }
                                Logger.printMessage("}");
                            } else {
                                property.setValue(args[2]);
                                Logger.printMessage(property.getLabel() + " has been set to " + property.getValue() + " for " + hudComponent.getLabel() + ".");
                            }
                        } else {
                            Logger.printMessage("Not enough arguments to change property.");
                        }
                    } else if (property instanceof ColorSetting) {
                        final ColorSetting colorSetting = (ColorSetting) property;
                        if (args.length >= 5) {
                            try {
                                final int r = MathUtil.clamp(Integer.parseInt(args[2]),0,255);
                                final int g = MathUtil.clamp(Integer.parseInt(args[3]),0,255);
                                final int b = MathUtil.clamp(Integer.parseInt(args[4]),0,255);
                                if (args.length > 5) {
                                    final int a = MathUtil.clamp(Integer.parseInt(args[5]),0,255);
                                    colorSetting.setValue(new Color(r,g,b,a));
                                } else {
                                    colorSetting.setValue(new Color(r,g,b));
                                }
                                Logger.printMessage(property.getLabel() + " has been set to " + colorSetting.getValue().getRGB() + " for " + hudComponent.getLabel() + ".");
                            } catch (Exception e) {
                                Logger.printMessage("Not enough arguments to change property.");
                            }
                        } else {
                            Logger.printMessage("Not enough arguments to change property.");
                        }
                    } else if (property instanceof StringSetting) {
                        if (args.length >= 3) {
                            final StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                stringBuilder.append(args[i]);
                                if (i != args.length - 1) stringBuilder.append(" ");
                            }
                            property.setValue(stringBuilder.toString());
                            Logger.printMessage(property.getLabel() + " has been set to " + property.getValue() + " for " + hudComponent.getLabel() + ".");
                        }
                    } else {
                        if (args.length >= 3) {
                            property.setValue(args[2]);
                            Logger.printMessage(property.getLabel() + " has been set to " + property.getValue() + " for " + hudComponent.getLabel() + ".");
                        } else {
                            Logger.printMessage("Not enough arguments to change property.");
                        }
                    }
                }
            });
        } else {
            StringBuilder builder = new StringBuilder(hudComponent.getLabel() + " " + ChatFormatting.PREFIX_CODE + "8[" + ChatFormatting.PREFIX_CODE + "7" + IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(hudComponent).size() + ChatFormatting.PREFIX_CODE + "8]" + ChatFormatting.PREFIX_CODE + "7: ");
            IngrosWare.INSTANCE.getSettingManager().getSettingsFromObject(hudComponent)
                    .forEach(property -> builder.append(ChatFormatting.PREFIX_CODE).append(property instanceof BooleanSetting ? (((BooleanSetting) property).getValue() ? "a" : "c") : "7").append(property.getLabel()).append(ChatFormatting.PREFIX_CODE + "8, "));
            Logger.printMessage(builder.toString().substring(0, builder.length() - 2));
        }
    }
}

