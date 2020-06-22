package best.reich.ingrosware.event.impl.render;

import net.minecraft.inventory.EntityEquipmentSlot;
import tcb.bces.event.EventCancellable;

public class RenderArmorEvent extends EventCancellable {

    public boolean shouldNotRenderArmor(EntityEquipmentSlot entityEquipmentSlot) {
        if (entityEquipmentSlot == EntityEquipmentSlot.HEAD)
            return true;
        if (entityEquipmentSlot == EntityEquipmentSlot.CHEST)
            return true;
        if (entityEquipmentSlot == EntityEquipmentSlot.LEGS)
            return true;
        if (entityEquipmentSlot == EntityEquipmentSlot.FEET)
            return true;
        return false;
    }
}
