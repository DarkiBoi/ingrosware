package best.reich.ingrosware.mixin.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.EventCape;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/17/2020
 **/
@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Shadow
    public abstract NetworkPlayerInfo getPlayerInfo();

    private EventCape eventCape;

    @Inject(method = "getLocationCape", at = @At("HEAD"))
    private void onLocationCapeHead(CallbackInfoReturnable<ResourceLocation> cir) {
        eventCape = new EventCape((AbstractClientPlayer) (Object) this);
        IngrosWare.INSTANCE.getBus().post(eventCape);
    }

    @Redirect(method = "getLocationCape", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetworkPlayerInfo;getLocationCape()v"))
    public ResourceLocation getLocationCape() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : eventCape.isCancelled() ? eventCape.getResourceLocation() : networkplayerinfo.getLocationCape();
    }
}
