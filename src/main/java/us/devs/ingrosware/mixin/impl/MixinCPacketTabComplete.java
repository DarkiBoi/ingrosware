package us.devs.ingrosware.mixin.impl;

import net.minecraft.network.play.client.CPacketTabComplete;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.devs.ingrosware.mixin.accessors.ICPacketTabComplete;

@Mixin(CPacketTabComplete.class)
public abstract class MixinCPacketTabComplete implements ICPacketTabComplete {

    @Override
    @Accessor
    public abstract void setMessage(String message);

}
