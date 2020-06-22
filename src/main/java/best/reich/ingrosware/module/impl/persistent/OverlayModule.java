package best.reich.ingrosware.module.impl.persistent;

import best.reich.ingrosware.module.annotation.Persistent;
import best.reich.ingrosware.module.types.PersistentModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.FullScreenEvent;
import best.reich.ingrosware.event.impl.render.Render2DEvent;
import best.reich.ingrosware.event.impl.other.ResizeEvent;
import best.reich.ingrosware.gui.hud.GuiHudEditor;
import best.reich.ingrosware.hud.Component;
import best.reich.ingrosware.hud.type.ClickableComponent;
import best.reich.ingrosware.module.ModuleCategory;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@Persistent(label = "Overlay", category = ModuleCategory.RENDER)
public class OverlayModule extends PersistentModule {

    @Subscribe
    public void onRender(Render2DEvent eventRender) {
        if (mc.gameSettings.showDebugInfo || mc.currentScreen instanceof GuiHudEditor) return;
        for(Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if(!(component instanceof ClickableComponent)) {
                if (component.getX() < 0)
                    component.setX(0);

                if (component.getX() + component.getWidth() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth())
                    component.setX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - component.getWidth());

                if (component.getY() < 0)
                    component.setY(0);

                if (component.getY() + component.getHeight() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight())
                    component.setY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - component.getHeight());

                if (!component.isHidden()) component.onDraw(new ScaledResolution(mc));
            }
        }
    }

    @Subscribe
    public void onScreenResize(ResizeEvent event) {
        for(Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if (!component.isHidden())
                component.onResize(event.getSr());
        }
    }

    @Subscribe
    public void onFullScreen(FullScreenEvent event) {
        for(Component component : IngrosWare.INSTANCE.getComponentManager().getValues()) {
            if (!component.isHidden())
                component.onFullScreen(event.getWidth(), event.getHeight());
        }
    }

}
