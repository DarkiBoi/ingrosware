package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.mixin.accessors.IPlayerControllerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.ClickBlockEvent;
import best.reich.ingrosware.event.impl.other.DamageBlockEvent;
import best.reich.ingrosware.event.impl.other.ResetBlockRemovingEvent;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP implements IPlayerControllerMP {

    @Accessor
    @Override
    public abstract void setBlockHitDelay(int blockHitDelay);

    @Accessor
    @Override
    public abstract void setCurBlockDamageMP(float curBlockDamageMP);

    @Accessor
    @Override
    public abstract float getCurBlockDamageMP();

    @Inject(method = "clickBlock", at = @At("HEAD"))
    private void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        IngrosWare.INSTANCE.getBus().post(new ClickBlockEvent(loc, face));
    }

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"))
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        IngrosWare.INSTANCE.getBus().post(new DamageBlockEvent(posBlock, directionFacing));
    }

    @Inject(method = "resetBlockRemoving", at = @At("HEAD"),cancellable = true)
    public void onResetBlockRemoving(CallbackInfo ci) {
        final ResetBlockRemovingEvent event = new ResetBlockRemovingEvent();
        IngrosWare.INSTANCE.getBus().post(event);
        if (event.isCancelled()) ci.cancel();
    }
}