package best.reich.ingrosware.mixin.impl;

import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.render.BossbarEvent;


@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo ci) {
        final BossbarEvent bossbar = new BossbarEvent();
        IngrosWare.INSTANCE.getBus().post(bossbar);
        if (bossbar.isCancelled()) {
            ci.cancel();
        }
    }
}
