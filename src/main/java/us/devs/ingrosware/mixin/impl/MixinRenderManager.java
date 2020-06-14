package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tcb.bces.event.EventType;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.render.RenderEntityEvent;
import us.devs.ingrosware.mixin.accessors.IRenderManager;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IRenderManager {
    @Override
    @Accessor
    public abstract double getRenderPosX();

    @Override
    @Accessor
    public abstract double getRenderPosY();

    @Override
    @Accessor
    public abstract double getRenderPosZ();

    @Redirect(method = "doRenderEntity", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/Render.doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
    private void doRenderEntity$doRender(Render render, Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        final RenderEntityEvent pre = new RenderEntityEvent(render, entity, x, y, z, entityYaw, partialTicks, EventType.PRE);
        IngrosWare.INSTANCE.getBus().post(pre);
        if (!pre.isCancelled()) {
            render.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
        final RenderEntityEvent post = new RenderEntityEvent(render, entity, x, y, z, entityYaw, partialTicks, EventType.POST);
        IngrosWare.INSTANCE.getBus().post(post);
    }
}