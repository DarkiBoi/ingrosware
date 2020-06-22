package best.reich.ingrosware.mixin.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.render.RenderChatBackgroundEvent;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void overrideChatBackgroundColour(int left, int top, int right, int bottom, int color) {
        RenderChatBackgroundEvent event = new RenderChatBackgroundEvent();
        IngrosWare.INSTANCE.getBus().post(event);
        if (!event.isCancelled())
            Gui.drawRect(left, top, right, bottom, color);
    }
}
