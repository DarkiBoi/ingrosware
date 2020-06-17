package us.devs.ingrosware.module.impl.persistent;

import net.minecraft.network.play.client.CPacketChatMessage;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.event.impl.network.PacketEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Persistent;
import us.devs.ingrosware.module.types.PersistentModule;
import us.devs.ingrosware.util.other.chat.ChatBuilder;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Persistent(label = "Commands", category = ModuleCategory.OTHER)
public class CommandsModule extends PersistentModule {

    @Subscribe
    public void onSendPacket(PacketEvent event) {
        if (event.getType() == EventType.PRE) {
            if (event.getPacket() instanceof CPacketChatMessage) {
                checkCommands(((CPacketChatMessage) event.getPacket()).getMessage(), event);
            }
        }
    }

    private void checkCommands(String message, PacketEvent event) {
        if (message.startsWith("-")) {
            String[] args = message.split(" ");
            String input = message.split(" ")[0].substring(1);
            for (Command command : IngrosWare.INSTANCE.getCommandManager().getValues()) {
                if (input.equalsIgnoreCase(command.getLabel())) {
                    event.setCancelled(true);
                    command.execute(args);
                } else {
                    for (String alias : command.getHandles()) {
                        if (input.equalsIgnoreCase(alias)) {
                            event.setCancelled(true);
                            command.execute(args);
                        }
                    }
                }
            }
            if (!event.isCancelled()) {
                new ChatBuilder().appendText("Command \"").appendText(message).appendText("\" was not found!").send();
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
    }

}
