package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import net.minecraft.util.text.event.ClickEvent;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.util.other.chat.ChatBuilder;
import best.reich.ingrosware.util.other.chat.ChatColor;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
@CommandManifest(label = "Modules", description = "Shows all toggleable modules.", handles = {"mods", "hacks", "cheats"})
public class ModulesCommand extends Command {

    @Override
    public void execute(String[] args) {
        final ChatBuilder chatBuilder = clientChatMsg();

        IngrosWare.INSTANCE.getModuleManager().getToggles().forEach(toggleableModule -> {
            chatBuilder.appendText(toggleableModule.getLabel(), toggleableModule.getState() ? ChatColor.GREEN : ChatColor.RED, new ClickEvent(ClickEvent.Action.RUN_COMMAND, "-toggle " + toggleableModule.getLabel()))
            .appendText(",", ChatColor.WHITE);
        });

        chatBuilder.send();
    }
}
