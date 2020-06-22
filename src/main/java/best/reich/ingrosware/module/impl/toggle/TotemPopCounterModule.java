package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.traits.Chatable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.event.impl.network.PacketEvent;
import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.util.other.chat.ChatColor;

import java.util.HashMap;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/16/2020
 **/
@Toggleable(label = "TotemPopCounter", category = ModuleCategory.OTHER,color = 0xff72AE90,bind = Keyboard.KEY_NONE)
public class TotemPopCounterModule extends ToggleableModule implements Chatable {
    public static HashMap<String, Integer> popList = new HashMap<>();

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        for(EntityPlayer player : mc.world.playerEntities) {
            if(player.getHealth() <= 0) {
                if(popList.containsKey(player.getName())) {
                    clientChatMsg().appendText(player.getName(), ChatColor.DARK_AQUA).appendText(" died after popping ", ChatColor.DARK_RED).appendText(popList.get(player.getDisplayNameString()) + " totems!", ChatColor.GOLD).send();
                    popList.remove(player.getName(), popList.get(player.getName()));
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.world == null || mc.player == null || event.getType() == EventType.PRE) return;
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                Entity entity = packet.getEntity(mc.world);
                if(popList == null) {
                    popList = new HashMap<>();
                }
                if(popList.get(entity.getName()) == null) {
                    popList.put(entity.getName(), 1);
                    clientChatMsg().appendText(entity.getName(), ChatColor.DARK_AQUA).appendText(" popped ", ChatColor.DARK_RED).appendText("1 totem!", ChatColor.GOLD).send();
                } else if(!(popList.get(entity.getName()) == null)) {
                    int popCounter = popList.get(entity.getName());
                    int newPopCounter = popCounter += 1;
                    popList.put(entity.getName(), newPopCounter);
                    clientChatMsg().appendText(entity.getName(), ChatColor.DARK_AQUA).appendText(" popped ", ChatColor.DARK_RED).appendText(newPopCounter + " totems!", ChatColor.GOLD).send();
                }
            }
        }
    }
}