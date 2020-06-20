package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.other.SetGameOptionEvent;

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
