package us.devs.ingrosware.module.impl.persistent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.other.FullScreenEvent;
import us.devs.ingrosware.event.impl.render.Render2DEvent;
import us.devs.ingrosware.event.impl.other.ResizeEvent;
import us.devs.ingrosware.gui.hud.GuiHudEditor;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.type.ClickableComponent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Persistent;
import us.devs.ingrosware.module.types.PersistentModule;

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
