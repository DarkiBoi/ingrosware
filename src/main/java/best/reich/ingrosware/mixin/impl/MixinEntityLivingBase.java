package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.mixin.accessors.IEntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.entity.JumpEvent;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity implements IEntityLivingBase {

    @Accessor
    @Override
    public abstract boolean getIsJumping();

    private float x,y,z;

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void onJump(CallbackInfo callbackInfo) {
        JumpEvent event = new JumpEvent(getX(),getY(),getZ());
        IngrosWare.INSTANCE.getBus().post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method = "jump", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;motionX:D"))
    private double jumpMotionX(Entity player) {
        return getX();
    }

    @Redirect(method = "jump", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;motionY:D"))
    private double jumpMotionY(Entity player) {
        return getY();
    }

    @Redirect(method = "jump", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;motionZ:D"))
    private double jumpMotionZ(Entity player) {
        return getZ();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
