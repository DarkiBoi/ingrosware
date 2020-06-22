package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.event.impl.other.GetRainStrengthEvent;
import best.reich.ingrosware.event.impl.other.WorldTimeEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import best.reich.ingrosware.IngrosWare;

@Mixin(World.class)
public class MixinWorld {
    
    @Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
    public void getRainStrength(float delta, CallbackInfoReturnable<Float> cir) {
        GetRainStrengthEvent getRainStrengthEvent = new GetRainStrengthEvent();
        IngrosWare.INSTANCE.getBus().post(getRainStrengthEvent);
        if (getRainStrengthEvent.isCancelled()) cir.setReturnValue(0f);
    }

    @Inject(method = "getWorldTime", at = @At("HEAD"), cancellable = true)
    public void getWorldTime(CallbackInfoReturnable<Long> cir) {
        WorldTimeEvent worldTimeEvent = new WorldTimeEvent();
        IngrosWare.INSTANCE.getBus().post(worldTimeEvent);
        if (worldTimeEvent.isCancelled() && worldTimeEvent.getWorldTime() != null) {
            cir.setReturnValue(worldTimeEvent.getWorldTime());
        }
    }

}
