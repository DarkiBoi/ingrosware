package us.devs.ingrosware.mixin.impl;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.devs.ingrosware.mixin.accessors.ICPacketPlayer;

@Mixin(CPacketPlayer.class)
public abstract class MixinCPacketPlayer implements ICPacketPlayer {

    @Override
    @Accessor
    public abstract void setOnGround(boolean onGround);

    @Override
    @Accessor
    public abstract void setYaw(float yaw);

    @Override
    @Accessor
    public abstract float getYaw();

    @Override
    @Accessor
    public abstract void setPitch(float pitch);

    @Override
    @Accessor
    public abstract float getPitch();
}