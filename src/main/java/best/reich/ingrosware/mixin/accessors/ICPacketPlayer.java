package best.reich.ingrosware.mixin.accessors;

public interface ICPacketPlayer {

    void setOnGround(boolean onGround);

    void setYaw(float yaw);

    float getYaw();

    void setPitch(float pitch);

    float getPitch();
}