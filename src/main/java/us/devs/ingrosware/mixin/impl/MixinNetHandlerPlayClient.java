package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.other.ChunkLoadEvent;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {

     @Inject(method = "handleChunkData", at = @At("HEAD"))
    private void handleChunkData(SPacketChunkData packetIn, CallbackInfo ci) {
        IngrosWare.INSTANCE.getBus().post(new ChunkLoadEvent(Minecraft.getMinecraft().world.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ()),packetIn.isFullChunk(), packetIn.getChunkX(), packetIn.getChunkZ()));
    }
}
