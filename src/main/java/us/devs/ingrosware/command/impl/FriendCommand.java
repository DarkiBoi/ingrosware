package us.devs.ingrosware.command.impl;

import net.minecraft.client.network.NetworkPlayerInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.command.Command;
import us.devs.ingrosware.command.annotation.CommandManifest;
import us.devs.ingrosware.friend.Friend;
import us.devs.ingrosware.util.other.ProfileHelper;
import us.devs.ingrosware.util.other.chat.ChatColor;

import java.util.Optional;
import java.util.UUID;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
@CommandManifest(label = "Friend", description = "Add amigos", handles = {"f", "amigo", "fri"})
public class FriendCommand extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2 || args.length > 3) {
            clientChatMsg().appendText("Invalid args", ChatColor.RED).send();
            return;
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "a":
            case "add": {
                final String nickName = (args.length == 3) ? args[2] : args[1];
                final String name = args[1];
                if (IngrosWare.INSTANCE.getFriendManager().get(nickName).isPresent()) {
                    clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", new ChatColor[0]).send();
                    return;
                }
                UUID uuid = ProfileHelper.INSTANCE.getUUID(name);
                if (uuid == null)
                    return;
                if (IngrosWare.INSTANCE.getFriendManager().get(uuid).isPresent()) {
                    clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", new ChatColor[0]).send();
                    return;
                }
                if(mc.getConnection().getPlayerInfo(name) != null) {
                    final NetworkPlayerInfo player = mc.getConnection().getPlayerInfo(name);
                    if (IngrosWare.INSTANCE.getFriendManager().get(player.getGameProfile().getId()).isPresent()) {
                        clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", new ChatColor[0]).send();
                        return;
                    }
                    IngrosWare.INSTANCE.getFriendManager().add(player.getGameProfile().getId(), nickName);
                } else {
                    clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" is not in the server, fetching id...", new ChatColor[0]).send();
                    uuid = ProfileHelper.INSTANCE.getUUID(name);
                    if (uuid == null) {
                        clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" does not exist.", new ChatColor[0]).send();
                        return;
                    }
                    if (IngrosWare.INSTANCE.getFriendManager().get(uuid).isPresent()) {
                        clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", new ChatColor[0]).send();
                        return;
                    }

                    IngrosWare.INSTANCE.getFriendManager().add(uuid, nickName);
                }
                clientChatMsg().appendText("Added ", new ChatColor[0]).appendText(nickName, ChatColor.GRAY).appendText(" as a friend.", new ChatColor[0]).send();
                break;
            }
            case "r":
            case "rem":
            case "delete":
            case "del":
            case "remove": {
                final String name = args[1];
                if (IngrosWare.INSTANCE.getFriendManager().get(name).isPresent()) {
                    final Friend friend = IngrosWare.INSTANCE.getFriendManager().get(name).get();
                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend);
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" as a friend.", new ChatColor[0]).send();
                    return;
                }

                if (mc.getConnection().getPlayerInfo(name) != null) {
                    final NetworkPlayerInfo player = mc.getConnection().getPlayerInfo(name);
                    final Friend friend = IngrosWare.INSTANCE.getFriendManager().get(player.getGameProfile().getId()).get();
                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend);
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" as a friend.", new ChatColor[0]).send();
                } else {
                    clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" is not in the server, fetching id...", new ChatColor[0]).send();
                    final UUID uuid = ProfileHelper.INSTANCE.getUUID(name);
                    if (uuid == null) {
                        clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" does not exist.", new ChatColor[0]).send();
                        return;
                    }
                    final Optional<Friend> friend = IngrosWare.INSTANCE.getFriendManager().get(uuid);
                    if (!friend.isPresent()) {
                        clientChatMsg().appendText("Player ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" is not a friend", new ChatColor[0]).send();
                        return;
                    }

                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend.get());
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", new ChatColor[0]).appendText(name, ChatColor.GRAY).appendText(" as a friend.", new ChatColor[0]).send();
                }
                break;
            }
        }
    }


}
