package us.devs.ingrosware.mixin.impl;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.devs.ingrosware.mixin.accessors.ICPacketCustomPayload;

@Mixin(CPacketCustomPayload.class)
public abstract class MixinCPacketCustomPayload implements ICPacketCustomPayload {

    @Override
    @Accessor
    public abstract void setData(PacketBuffer data);

}