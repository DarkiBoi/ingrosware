package best.reich.ingrosware.mixin.impl;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tcb.bces.event.EventType;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.EntityChunkEvent;

@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    private void onEntityAdded(Entity entity, CallbackInfo info) {
        IngrosWare.INSTANCE.getBus().post(new EntityChunkEvent(EventType.PRE, entity));
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    private void onEntityRemoved(Entity entity, CallbackInfo info) {
        IngrosWare.INSTANCE.getBus().post(new EntityChunkEvent(EventType.POST, entity));
    }
}