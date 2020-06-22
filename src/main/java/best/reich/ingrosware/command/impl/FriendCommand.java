package best.reich.ingrosware.command.impl;

import best.reich.ingrosware.command.annotation.CommandManifest;
import net.minecraft.client.network.NetworkPlayerInfo;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.command.Command;
import best.reich.ingrosware.friend.Friend;
import best.reich.ingrosware.util.other.ProfileHelper;
import best.reich.ingrosware.util.other.chat.ChatColor;

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
        if (args.length < 2 || args.length > 4) {
            clientChatMsg().appendText("Invalid args", ChatColor.RED).send();
            return;
        }
        final String lowerCase = args[1].toLowerCase();
        switch (lowerCase) {
            case "a":
            case "add": {
                final String nickName = (args.length == 4) ? args[3] : args[2];
                final String name = args[2];
                if (IngrosWare.INSTANCE.getFriendManager().get(nickName).isPresent()) {
                    clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", ChatColor.WHITE).send();
                    return;
                }
                UUID uuid = ProfileHelper.INSTANCE.getUUID(name);
                if (uuid == null)
                    return;
                if (IngrosWare.INSTANCE.getFriendManager().get(uuid).isPresent()) {
                    clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", ChatColor.WHITE).send();
                    return;
                }
                if(mc.getConnection().getPlayerInfo(name) != null) {
                    final NetworkPlayerInfo player = mc.getConnection().getPlayerInfo(name);
                    if (IngrosWare.INSTANCE.getFriendManager().get(player.getGameProfile().getId()).isPresent()) {
                        clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", ChatColor.WHITE).send();
                        return;
                    }
                    IngrosWare.INSTANCE.getFriendManager().add(player.getGameProfile().getId(), nickName);
                } else {
                    clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" is not in the server, fetching id...", ChatColor.WHITE).send();
                    uuid = ProfileHelper.INSTANCE.getUUID(name);
                    if (uuid == null) {
                        clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" does not exist.", ChatColor.WHITE).send();
                        return;
                    }
                    if (IngrosWare.INSTANCE.getFriendManager().get(uuid).isPresent()) {
                        clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(nickName, ChatColor.GRAY).appendText(" is already a friend.", ChatColor.WHITE).send();
                        return;
                    }

                    IngrosWare.INSTANCE.getFriendManager().add(uuid, nickName);
                }
                clientChatMsg().appendText("Added ", ChatColor.WHITE).appendText(nickName, ChatColor.GRAY).appendText(" as a friend.", ChatColor.WHITE).send();
                break;
            }
            case "r":
            case "rem":
            case "delete":
            case "del":
            case "remove": {
                final String name = args[2];
                if (IngrosWare.INSTANCE.getFriendManager().get(name).isPresent()) {
                    final Friend friend = IngrosWare.INSTANCE.getFriendManager().get(name).get();
                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend);
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" as a friend.", ChatColor.WHITE).send();
                    return;
                }

                if (mc.getConnection().getPlayerInfo(name) != null) {
                    final NetworkPlayerInfo player = mc.getConnection().getPlayerInfo(name);
                    final Friend friend = IngrosWare.INSTANCE.getFriendManager().get(player.getGameProfile().getId()).get();
                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend);
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" as a friend.", ChatColor.WHITE).send();
                } else {
                    clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" is not in the server, fetching id...", ChatColor.WHITE).send();
                    final UUID uuid = ProfileHelper.INSTANCE.getUUID(name);
                    if (uuid == null) {
                        clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" does not exist.", ChatColor.WHITE).send();
                        return;
                    }
                    final Optional<Friend> friend = IngrosWare.INSTANCE.getFriendManager().get(uuid);
                    if (!friend.isPresent()) {
                        clientChatMsg().appendText("Player ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" is not a friend", ChatColor.WHITE).send();
                        return;
                    }

                    IngrosWare.INSTANCE.getFriendManager().getList().remove(friend.get());
                    IngrosWare.INSTANCE.getFriendManager().save();
                    clientChatMsg().appendText("Removed ", ChatColor.WHITE).appendText(name, ChatColor.GRAY).appendText(" as a friend.", ChatColor.WHITE).send();
                }
                break;
            }
            case "clear":
            case "cl":
            case "removeall": {
                if(IngrosWare.INSTANCE.getFriendManager().isEmpty()) {
                    clientChatMsg().appendText("Your friends list is empty").send();
                    return;
                }
                clientChatMsg().appendText("Your have cleared your friends list. Friends removed: ").appendText(String.valueOf(IngrosWare.INSTANCE.getFriendManager().size())).send();
                IngrosWare.INSTANCE.getFriendManager().clear();
                IngrosWare.INSTANCE.getFriendManager().save();
                break;
            }
            case "l":
            case "show":
            case "list": {
                if(IngrosWare.INSTANCE.getFriendManager().isEmpty()) {
                    clientChatMsg().appendText("Your friends list is empty").send();
                    return;
                }
                clientChatMsg().appendText("Your current friends are: ").send();
                IngrosWare.INSTANCE.getFriendManager().getList().forEach(friend ->
                        clientChatMsg().appendText("Username: " + friend.getName()).appendText(", Player Name: " + friend.getPlayerName()).appendText(", UUID: " + friend.getUUID()).send());
                break;
            }
        }
    }


}
