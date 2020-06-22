package best.reich.ingrosware.util.other.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
public class ChatBuilder {
    private TextComponentString message;

    public ChatBuilder() {
        this.message = new TextComponentString("");
    }

    public ChatBuilder appendPrefix() {
        this.appendText("Ingros", ChatColor.GRAY).appendText(" | ", ChatColor.DARK_GRAY);
        return this;
    }

    public ChatBuilder appendText(final String text, final ChatColor... colors) {
        final TextComponentString componentText = new TextComponentString(text);
        Style chatStyle = new Style();
        for (final ChatColor color : colors) {
            switch (color) {
                case BOLD: {
                    chatStyle.setBold(true);
                    break;
                }
                case UNDERLINE: {
                    chatStyle.setUnderlined(true);
                    break;
                }
                case ITALIC: {
                    chatStyle.setItalic(true);
                    break;
                }
                case STRIKETHROUGH: {
                    chatStyle.setStrikethrough(true);
                    break;
                }
                case OBFUSCATED: {
                    chatStyle.setObfuscated(true);
                    break;
                }
                case RESET: {
                    chatStyle = new Style();
                    break;
                }
                default: {
                    chatStyle.setColor(color.getBaseColor());
                    break;
                }
            }
        }
        componentText.setStyle(chatStyle);
        this.message.appendSibling(componentText);
        return this;
    }

    public ChatBuilder appendText(final String text, final ChatColor color, final HoverEvent hoverEvent) {
        final TextComponentString componentText = new TextComponentString(text);
        Style chatStyle = new Style();
        switch (color) {
            case BOLD: {
                chatStyle.setBold(true);
                break;
            }
            case UNDERLINE: {
                chatStyle.setUnderlined(true);
                break;
            }
            case ITALIC: {
                chatStyle.setItalic(true);
                break;
            }
            case STRIKETHROUGH: {
                chatStyle.setStrikethrough(true);
                break;
            }
            case OBFUSCATED: {
                chatStyle.setObfuscated(true);
                break;
            }
            case RESET: {
                chatStyle = new Style();
                break;
            }
            default: {
                chatStyle.setColor(color.getBaseColor());
                break;
            }
        }
        chatStyle.setHoverEvent(hoverEvent);
        componentText.setStyle(chatStyle);
        this.message.appendSibling(componentText);
        return this;
    }

    public ChatBuilder appendText(final String text, final ChatColor color, final ClickEvent clickEvent) {
        final TextComponentString componentText = new TextComponentString(text);
        Style chatStyle = new Style();
        switch (color) {
            case BOLD: {
                chatStyle.setBold(true);
                break;
            }
            case UNDERLINE: {
                chatStyle.setUnderlined(true);
                break;
            }
            case ITALIC: {
                chatStyle.setItalic(true);
                break;
            }
            case STRIKETHROUGH: {
                chatStyle.setStrikethrough(true);
                break;
            }
            case OBFUSCATED: {
                chatStyle.setObfuscated(true);
                break;
            }
            case RESET: {
                chatStyle = new Style();
                break;
            }
            default: {
                chatStyle.setColor(color.getBaseColor());
                break;
            }
        }
        chatStyle.setClickEvent(clickEvent);
        componentText.setStyle(chatStyle);
        this.message.appendSibling(componentText);
        return this;
    }

    public ChatBuilder appendTextF(final String text, final Object... formatted) {
        final TextComponentString componentText = new TextComponentString(String.format(text, formatted));
        this.message.appendSibling(componentText);
        return this;
    }

    public void send() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
    }

    public TextComponentString getMessage() {
        return this.message;
    }

}
