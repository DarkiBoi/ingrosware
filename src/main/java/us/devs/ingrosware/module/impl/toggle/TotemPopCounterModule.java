package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.entity.UpdateEvent;
import us.devs.ingrosware.event.impl.network.PacketEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.other.chat.ChatBuilder;
import us.devs.ingrosware.util.other.chat.ChatColor;

import java.util.HashMap;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/16/2020
 **/
@Toggleable(label = "TotemPopCounter", category = ModuleCategory.OTHER,color = 0xff72AE90,bind = Keyboard.KEY_NONE)
public class TotemPopCounterModule extends ToggleableModule {
    public static HashMap<String, Integer> popList = new HashMap<>();

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        for(EntityPlayer player : mc.world.playerEntities) {
            if(player.getHealth() <= 0) {
                if(popList.containsKey(player.getName())) {
                    new ChatBuilder().appendText(player.getName(), ChatColor.DARK_AQUA).appendText(" died after popping ",ChatColor.DARK_RED).appendText(popList.get(player.getName()) + " totems!",ChatColor.GOLD).send();
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
                    new ChatBuilder().appendText(entity.getName(), ChatColor.DARK_AQUA).appendText(" popped ",ChatColor.DARK_RED).appendText("1 totem!",ChatColor.GOLD).send();
                } else if(!(popList.get(entity.getName()) == null)) {
                    int popCounter = popList.get(entity.getName());
                    int newPopCounter = popCounter += 1;
                    popList.put(entity.getName(), newPopCounter);
                    new ChatBuilder().appendText(entity.getName(), ChatColor.DARK_AQUA).appendText(" popped ",ChatColor.DARK_RED).appendText(newPopCounter + " totems!",ChatColor.GOLD).send();
                }
            }
        }
    }
}