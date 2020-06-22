package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.SetGameOptionEvent;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author TBM
 * @since 6/20/2020
 **/
@Mixin(GameSettings.class)
public class MixinGameSettings {

    @Inject(method = "setOptionValue", at = @At("HEAD"), cancellable = true)
    public void setOptionValue(GameSettings.Options settingsOption, int value, CallbackInfo ci) {
        SetGameOptionEvent event = new SetGameOptionEvent(settingsOption, value);
        IngrosWare.INSTANCE.getBus().post(event);
        if (event.isCancelled()) ci.cancel();
    }

}
