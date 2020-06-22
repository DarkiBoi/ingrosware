package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.util.other.chat.ChatBuilder;
import best.reich.ingrosware.util.other.chat.ChatColor;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@CommandManifest(label = "Help", description = "Shows all commands", handles = {"commands", "h"})
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        final ChatBuilder chatBuilder = clientChatMsg();

        IngrosWare.INSTANCE.getCommandManager().getValues().forEach(command -> {
            chatBuilder.appendText(command.getLabel(), ChatColor.GRAY, new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(command.getDescription()))).appendText(", ", ChatColor.WHITE);
        });
        chatBuilder.send();
    }
}
