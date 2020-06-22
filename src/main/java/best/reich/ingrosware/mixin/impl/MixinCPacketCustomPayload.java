package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.mixin.accessors.ICPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketCustomPayload.class)
public abstract class MixinCPacketCustomPayload implements ICPacketCustomPayload {

    @Override
    @Accessor
    public abstract void setData(PacketBuffer data);

}