package us.devs.ingrosware.mixin.impl;

import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.entity.MoveStateEvent;


@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions {
    @Inject(method = "updatePlayerMoveState", at = @At("RETURN"))
    public void updatePlayerMoveStateReturn(CallbackInfo callback) {
        IngrosWare.INSTANCE.getBus().post(new MoveStateEvent());
    }
}