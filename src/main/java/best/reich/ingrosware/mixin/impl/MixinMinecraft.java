package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.mixin.accessors.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.FullScreenEvent;
import best.reich.ingrosware.event.impl.other.KeyPressEvent;
import best.reich.ingrosware.event.impl.other.ResizeEvent;
import best.reich.ingrosware.event.impl.other.TickEvent;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft {
    @Shadow
    public int displayWidth;
    @Shadow
    public int displayHeight;

    @Accessor
    @Override
    public abstract void setSession(Session session);

    @Accessor
    @Override
    public abstract void setRightClickDelayTimer(int delay);

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        IngrosWare.INSTANCE.start();
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    private void shutdownMinecraftApplet(CallbackInfo ci) {
        IngrosWare.INSTANCE.close();
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, shift = At.Shift.BEFORE))
    private void onKeyboard(CallbackInfo callbackInfo) {
        final int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (Keyboard.getEventKeyState())
            IngrosWare.INSTANCE.getBus().post(new KeyPressEvent(i));
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        IngrosWare.INSTANCE.getBus().post(new TickEvent());
    }

    @Inject(method = "toggleFullscreen", at = @At("RETURN"))
    private void onToggleFullscreen(CallbackInfo info) {
        IngrosWare.INSTANCE.getBus().post(new FullScreenEvent(displayWidth, displayHeight));
    }
    @Inject(method = "resize", at = @At("RETURN"))
    private void onResize(CallbackInfo info) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final ResizeEvent rsevent = new ResizeEvent(scaledresolution);
            IngrosWare.INSTANCE.getBus().post(rsevent);
        }
    }
}
