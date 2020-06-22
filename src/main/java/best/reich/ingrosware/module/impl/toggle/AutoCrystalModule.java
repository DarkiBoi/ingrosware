package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Clamp;
import best.reich.ingrosware.setting.annotation.Mode;
import best.reich.ingrosware.setting.annotation.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import org.lwjgl.input.Keyboard;
import tcb.bces.event.EventType;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.entity.UpdateEvent;
import best.reich.ingrosware.event.impl.network.PacketEvent;
import best.reich.ingrosware.event.impl.render.Render3DEvent;
import best.reich.ingrosware.mixin.accessors.IRenderManager;
import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.util.math.TimerUtil;
import best.reich.ingrosware.util.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/14/2020
 **/
@Toggleable(label = "AutoCrystal", category = ModuleCategory.COMBAT, color = 0xffff0000, bind = Keyboard.KEY_NONE)
public class AutoCrystalModule extends ToggleableModule {
    private EntityLivingBase target;
    private BlockPos crystalPos;
    @Setting("T-Sort-Mode")
    @Mode({"FOV", "HEALTH", "DISTANCE"})
    public String targetSortMode = "DISTANCE";
    @Clamp(minimum = "0.1", maximum = "7.0")
    @Setting("Crystal-Range")
    public float crystalRange = 6.0f;
    @Clamp(minimum = "0.1", maximum = "20.0")
    @Setting("Target-Range")
    public double targetRange = 10.0;
    @Clamp(minimum = "0.1", maximum = "7.0")
    @Setting("Break-Trace")
    public double breakTrace = 6.0;
    @Clamp(minimum = "0.1", maximum = "20.0")
    @Setting("Min-Damage")
    public float minDamage = 6.0f;
    @Clamp(minimum = "0.1", maximum = "20.0")
    @Setting("Face-Damage")
    public float faceDamage = 4.0f;
    @Clamp(minimum = "0.1", maximum = "20.0")
    @Setting("Max-Self-Damage")
    public double maxSelfDamage = 8.0f;
    @Clamp(maximum = "500")
    @Setting("Place-Delay")
    public int placeDelay = 100;
    @Clamp(maximum = "500")
    @Setting("Break-Delay")
    public int breakDelay = 0;
    @Setting("Color")
    public Color color = new Color(255, 0, 255);
    @Setting("Anti-Suicide")
    public boolean antiSuicide = true;
    @Setting("Anti-Surround")
    public boolean antiSurround = true;
    @Setting("Mine-Stop")
    public boolean mineStop = true;
    @Setting("Food-Stop")
    public boolean foodStop = true;
    @Setting("Crystal-Switch")
    public boolean crystalSwitch = true;
    @Setting("Raytrace")
    public boolean rayTrace = false;
    @Setting("Rotate")
    public boolean rotate = true;
    @Setting("Show-Rotations")
    public boolean showRotations = false;
    @Setting("Invisibles")
    public boolean invisibles = true;
    @Setting("Players")
    public boolean players = true;
    @Setting("Animals")
    public boolean animals = false;
    @Setting("Monsters")
    public boolean monsters = false;
    @Setting("Passives")
    public boolean passives = false;
    private final TimerUtil placeTimer = new TimerUtil();
    private final TimerUtil breakTimer = new TimerUtil();

    @Override
    public void onState() {
        placeTimer.reset();
        breakTimer.reset();
        crystalPos = null;
        target = null;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.world == null || mc.player == null) return;
        int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        final boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        if (!offhand && crystalSlot == -1) return;
        target = getTarget();
        if (target != null) {
            final EntityEnderCrystal enderCrystal = getCrystal();
            if (enderCrystal == null) crystalPos = getPlacePosition();
            else crystalPos = null;
            if (crystalPos == null) {
                placeTimer.reset();
            }
            if (enderCrystal == null) {
                breakTimer.reset();
            }
            if (event.getType() == EventType.PRE) {
                if (rotate) {
                    if (enderCrystal != null) {
                        final float[] crystalRotations = getRotationsToward(enderCrystal.getPosition());
                        if (showRotations) {
                            mc.player.rotationYaw = crystalRotations[0];
                            mc.player.rotationPitch = crystalRotations[1];
                        } else {
                            event.setYaw(crystalRotations[0]);
                            event.setPitch(crystalRotations[1]);
                        }
                    }
                    if (crystalPos != null) {
                        final float[] crystalPosRotations = getRotationsToward(crystalPos);
                        if (showRotations) {
                            mc.player.rotationYaw = crystalPosRotations[0];
                            mc.player.rotationPitch = crystalPosRotations[1];
                        } else {
                            event.setYaw(crystalPosRotations[0]);
                            event.setPitch(crystalPosRotations[1]);
                        }
                    }
                }
            } else {
                if (enderCrystal != null && breakTimer.reach(breakDelay)) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(enderCrystal));
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    breakTimer.reset();
                }
                if (crystalPos != null && placeTimer.reach(placeDelay)) {
                    if (!offhand && crystalSwitch && mc.player.inventory.currentItem != crystalSlot && !(foodStop && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive()) && !(mineStop && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && mc.player.isSwingInProgress)) {
                        mc.player.inventory.currentItem = crystalSlot;
                        return;
                    }
                    placeCrystalOnBlock(crystalPos, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                    placeTimer.reset();
                }
            }
        } else {
            breakTimer.reset();
            placeTimer.reset();
            crystalPos = null;
        }
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (target != null) {
            final double posXTarget = RenderUtil.interpolate(target.posX, target.lastTickPosX, event.getPartialTicks()) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
            final double posYTarget = RenderUtil.interpolate(target.posY, target.lastTickPosY, event.getPartialTicks()) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
            final double posZTarget = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, event.getPartialTicks()) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
            RenderUtil.drawESP(new AxisAlignedBB(0.0, 0.0, 0.0, target.width, target.height, target.width).offset(posXTarget - target.width / 2, posYTarget, posZTarget - target.width / 2), color.getRed(), color.getGreen(), color.getBlue(), 40F);
            RenderUtil.drawESPOutline(new AxisAlignedBB(0.0, 0.0, 0.0, target.width, target.height, target.width).offset(posXTarget - target.width / 2, posYTarget, posZTarget - target.width / 2), color.getRed(), color.getGreen(), color.getBlue(), 255f, 1f);
            if (crystalPos != null) {
                final AxisAlignedBB bb = new AxisAlignedBB(crystalPos.getX() - mc.getRenderManager().viewerPosX, crystalPos.getY() - mc.getRenderManager().viewerPosY + 1.0, crystalPos.getZ() - mc.getRenderManager().viewerPosZ, crystalPos.getX() + 1 - mc.getRenderManager().viewerPosX, crystalPos.getY() + 1.2 - mc.getRenderManager().viewerPosY, crystalPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
                if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
                    final double posX = crystalPos.getX() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
                    final double posY = crystalPos.getY() - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
                    final double posZ = crystalPos.getZ() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
                    RenderUtil.drawESP(bb, (float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 40.0f);
                    RenderUtil.drawESPOutline(bb, (float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), 255.0f, 1.0f);
                    RenderUtil.renderTag(Math.floor(calculateDamage(crystalPos, target)) + "hp", posX + 0.5, posY, posZ + 0.5, color.getRGB());
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderHelper.disableStandardItemLighting();
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.world == null || mc.player == null) return;
        if (event.getType() == EventType.POST && event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                        e.setDead();
                    }
                }
            }
        }
    }

    private EntityEnderCrystal getCrystal() {
        double minVal = Double.POSITIVE_INFINITY;
        float damage = minDamage - 0.5f;
        Entity bestEntity = null;
        for (final Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal) {
                final double val = target.getDistanceSqToEntity(e);
                if (!isValidCrystal(e) || val >= minVal) continue;
                final float targetDamage = calculateDamage(e, target);
                final float selfDamage = calculateDamage(e, mc.player);
                if ((targetDamage <= (isInHole(target) ? faceDamage : damage) && (!(targetDamage > damage && targetDamage >= target.getHealth() + target.getAbsorptionAmount()))) || (antiSuicide && targetDamage <= selfDamage) || selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount() || (antiSuicide && selfDamage >= maxSelfDamage))
                    continue;
                minVal = val;
                bestEntity = e;
                damage = targetDamage;
            }
        }
        return (EntityEnderCrystal) bestEntity;
    }

    private EntityLivingBase getTarget() {
        double minVal = Double.POSITIVE_INFINITY;
        Entity bestEntity = null;
        for (final Entity e : mc.world.loadedEntityList) {
            final double val = getSortingWeightTarget(e);
            if (isValidEntity(e) && val < minVal) {
                minVal = val;
                bestEntity = e;
            }
        }
        return (EntityLivingBase) bestEntity;
    }

    private BlockPos getPlacePosition() {
        BlockPos placePosition = null;
        float damage = minDamage - 0.5f;
        for (final BlockPos pos : possiblePlacePositions(crystalRange, antiSurround)) {
            final float targetDamage = calculateDamage(pos, target);
            final float selfDamage = calculateDamage(pos, mc.player);
            if ((targetDamage <= (isInHole(target) ? faceDamage : damage) && (!(targetDamage > damage && targetDamage >= target.getHealth() + target.getAbsorptionAmount()))) || (antiSuicide && targetDamage <= selfDamage) || selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount() || (antiSuicide && selfDamage >= maxSelfDamage))
                continue;
            placePosition = pos;
            damage = targetDamage;
        }
        return placePosition;
    }

    private double getSortingWeightTarget(final Entity e) {
        final String upperCase = targetSortMode.toUpperCase();
        switch (upperCase) {
            case "FOV":
                return yawDist(e);
            case "HEALTH":
                return (e instanceof EntityLivingBase) ? ((EntityLivingBase) e).getHealth() : Double.POSITIVE_INFINITY;
            default:
                return mc.player.getDistanceSqToEntity(e);
        }
    }
    private double yawDist(final Entity e) {
        if (e != null) {
            final Vec3d difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.player.getPositionVector().addVector(0.0, mc.player.getEyeHeight(), 0.0));
            final double d = Math.abs(mc.player.rotationYaw - (Math.toDegrees(Math.atan2(difference.z, difference.x)) - 90.0)) % 360.0;
            return (d > 180.0) ? (360.0 - d) : d;
        }
        return 0.0;
    }

    private boolean isValidEntity(final Entity entity) {
        return (entity instanceof EntityLivingBase && entity.getEntityId() != -1488 && entity != mc.player && entity.isEntityAlive() && !IngrosWare.INSTANCE.getFriendManager().get(entity.getUniqueID()).isPresent() && (!entity.isInvisible() || invisibles) && mc.player.getDistanceSqToEntity(entity) <= targetRange * targetRange && ((entity instanceof EntityPlayer && players) || ((entity instanceof EntityMob || entity instanceof EntityGolem) && monsters) || (entity instanceof EntityAnimal && animals))) || (passives && (entity instanceof EntityIronGolem || entity instanceof EntityAmbientCreature));
    }

    private boolean isValidCrystal(final Entity entity) {
        return entity != null && mc.player.getDistanceSqToEntity(entity) <= crystalRange * crystalRange && (rayTrace || mc.player.canEntityBeSeen(entity) || (!mc.player.canEntityBeSeen(entity) && mc.player.getDistanceSqToEntity(entity) <= breakTrace * breakTrace));
    }

    private float[] getRotationsToward(final BlockPos blockPos) {
        final double xDist = blockPos.getX() + 0.5f - mc.player.posX;
        final double yDist = blockPos.getY() - (mc.player.posY + mc.player.getEyeHeight());
        final double zDist = blockPos.getZ() + 0.5f - mc.player.posZ;
        final double fDist = MathHelper.sqrt(xDist * xDist + zDist * zDist);
        final float yaw = fixRotation(mc.player.rotationYaw, (float) (MathHelper.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f);
        final float pitch = fixRotation(mc.player.rotationPitch, (float) (-(MathHelper.atan2(yDist, fDist) * 180.0 / 3.141592653589793)));
        return new float[]{yaw, pitch};
    }

    private float fixRotation(final float p_70663_1_, final float p_70663_2_) {
        float var4 = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
        if (var4 > 360.0f) {
            var4 = 360.0f;
        }
        if (var4 < -360.0f) {
            var4 = -360.0f;
        }
        return p_70663_1_ + var4;
    }

    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception ignored) {
        }
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float) (int) ((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float) finald;
    }

    private float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer) entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception ignored) {
            }
            final float f = MathHelper.clamp((float) k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getDamageMultiplied(final float damage) {
        final int diff = mc.world.getDifficulty().getDifficultyId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }

    private float calculateDamage(final Entity crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private ArrayList<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int) r; x <= cx + r; ++x) {
            for (int z = cz - (int) r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int) r) : cy; y < (sphere ? (cy + r) : ((float) (cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private NonNullList<BlockPos> possiblePlacePositions(final float placeRange, final boolean specialEntityCheck) {
        final NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), placeRange, (int) placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, specialEntityCheck)).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                return false;
            }
            if (!specialEntityCheck) {
                return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }
            for (final Entity entity : mc.world.getEntitiesWithinAABB((Class<? extends Entity>) Entity.class, new AxisAlignedBB(boost))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            for (final Entity entity : mc.world.getEntitiesWithinAABB((Class<? extends Entity>) Entity.class, new AxisAlignedBB(boost2))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private boolean isInHole(Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    private boolean isBlockValid(BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }

    private boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }

        return true;
    }

    private boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }

        return true;
    }

    private boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

    private float calculateDamage(final BlockPos pos, final Entity entity) {
        return calculateDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, entity);
    }

    private void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand) {
        final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }
}