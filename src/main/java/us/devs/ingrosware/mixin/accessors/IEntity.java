package us.devs.ingrosware.mixin.accessors;

public interface IEntity {

    float getRotationYaw();

    float getRotationPitch();

    double getPositionX();

    double getPositionY();

    double getPositionZ();

    boolean getPositionOnGround();
}
