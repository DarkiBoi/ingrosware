package best.reich.ingrosware.mixin.impl;

import best.reich.ingrosware.mixin.accessors.ISPacketPosLook;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketPlayerPosLook.class)
public abstract class MixinSPacketPosLook implements ISPacketPosLook {

    @Override
    @Accessor
    public abstract void setYaw(float yaw);

    @Override
    @Accessor
    public abstract void setPitch(float pitch);

}
