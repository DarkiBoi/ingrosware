package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.render.RenderNameEvent;

@Mixin(Render.class)
public class MixinRender {

    @Inject(method = "renderLivingLabel", at = @At("HEAD"), cancellable = true)
    private void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo ci) {
        RenderNameEvent event = new RenderNameEvent();
        IngrosWare.INSTANCE.getBus().post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}