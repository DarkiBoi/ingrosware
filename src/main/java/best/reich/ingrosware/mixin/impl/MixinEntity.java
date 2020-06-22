package best.reich.ingrosware.mixin.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.SafeWalkEvent;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Mixin(value = Entity.class, priority = 1001)
public class MixinEntity {
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public boolean onGround;
    @Shadow
    public boolean inPortal;
    @Shadow
    public void move(MoverType type, double x, double y, double z) {}

    public float getRotationYaw() {
        return rotationYaw;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public double getPositionX() {
        return posX;
    }

    public double getPositionY() {
        return posY;
    }

    public double getPositionZ() {
        return posZ;
    }

    public boolean getPositionOnGround() {
        return onGround;
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(Entity entity) {
        SafeWalkEvent event = new SafeWalkEvent();
        IngrosWare.INSTANCE.getBus().post(event);
        return event.isCancelled() || entity.isSneaking();
    }
}
