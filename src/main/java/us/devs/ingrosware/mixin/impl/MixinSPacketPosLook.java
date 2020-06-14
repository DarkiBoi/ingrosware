package us.devs.ingrosware.mixin.impl;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.devs.ingrosware.mixin.accessors.ISPacketPosLook;

@Mixin(SPacketPlayerPosLook.class)
public abstract class MixinSPacketPosLook implements ISPacketPosLook {

    @Override
    @Accessor
    public abstract void setYaw(float yaw);

    @Override
    @Accessor
    public abstract void setPitch(float pitch);

}
