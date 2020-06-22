package best.reich.ingrosware.mixin.impl;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.OpaqueEvent;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Mixin(EntityPlayer.class)
public class MixinEntityPlayer extends MixinEntity {

    @Inject(method = "isEntityInsideOpaqueBlock",at = @At("HEAD"),cancellable = true)
    private void onIsEntityInsideOpaqueBlock(CallbackInfoReturnable<Boolean> cir) {
        OpaqueEvent insideBlockRenderEvent = new OpaqueEvent();
        IngrosWare.INSTANCE.getBus().post(insideBlockRenderEvent);
        if(insideBlockRenderEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }


    @ModifyConstant(method = "attackTargetEntityWithCurrentItem", constant = { @Constant(doubleValue = 0.6) })
    private double decelerate(final double original) {
        return 1.0;
    }
}
