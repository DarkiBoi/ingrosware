package best.reich.ingrosware.module.impl.toggle;

import best.reich.ingrosware.module.annotation.Toggleable;
import best.reich.ingrosware.module.types.ToggleableModule;
import best.reich.ingrosware.setting.annotation.Setting;
import com.google.common.collect.ImmutableMap;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector4f;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.render.Render2DEvent;
import best.reich.ingrosware.event.impl.render.RenderNameEvent;
import best.reich.ingrosware.friend.Friend;
import best.reich.ingrosware.module.ModuleCategory;
import best.reich.ingrosware.util.math.MathUtil;
import best.reich.ingrosware.util.render.GLUProjection;
import best.reich.ingrosware.util.render.RenderUtil;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/16/2020
 **/
@Toggleable(label = "Nametags", category = ModuleCategory.RENDER, color = 0xff3f33ff, bind = Keyboard.KEY_NONE, hidden = true)
public class NametagsModule extends ToggleableModule {
    @Setting("PlayerColor")
    public Color playerColor = new Color(255, 0, 0);
    @Setting("Armor")
    public boolean armor = true;
    @Setting("Ping")
    public boolean ping = true;
    @Setting("Invisibles")
    public boolean invisibles = true;
    @Setting("Players")
    public boolean players = true;
    @Setting("Animals")
    public boolean animals;
    @Setting("Monsters")
    public boolean monsters;
    @Setting("Passives")
    public boolean passives;
    @Setting("Rank")
    public boolean rank = true;

    private final ImmutableMap<String, String> cachedEnchantmentMap = new ImmutableMap.Builder<String, String>()
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)).getName(), "p").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(10)).getName(), "cob").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(1)).getName(), "fp").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(2)).getName(), "ff").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(3)).getName(), "bp").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(4)).getName(), "pp").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(5)).getName(), "r").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(6)).getName(), "aa").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)).getName(), "t").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(8)).getName(), "ds").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(9)).getName(), "fw")
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)).getName(), "s").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(22)).getName(), "se").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(17)).getName(), "sm").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(18)).getName(), "boa").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)).getName(), "kb").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)).getName(), "fa").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(21)).getName(), "l")
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(32)).getName(), "e").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(33)).getName(), "st").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(35)).getName(), "f")
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)).getName(), "pow").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)).getName(), "pun").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)).getName(), "fl").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(51)).getName(), "inf")
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(61)).getName(), "lu").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(62)).getName(), "lots")
            .put(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)).getName(), "un").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(70)).getName(), "m").put(Objects.requireNonNull(Enchantment.getEnchantmentByID(71)).getName(), "vc").build();

    @Subscribe
    public void onRenderName(RenderNameEvent event) {
        event.setCancelled(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        if (mc.world == null || mc.player == null) return;
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        mc.world.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && ent.getUniqueID() != mc.player.getUniqueID() && RenderUtil.isInViewFrustrum(ent)) {
                    final Color clr = getEntityColor(entity);
                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks();
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks();
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks();
                    final AxisAlignedBB bb = entity.getEntityBoundingBox().expand(0.1, 0.1, 0.1);
                    final Vector3d[] corners = {new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.minZ - bb.maxZ + entity.width / 2.0f), new Vector3d(posX + bb.minX - bb.maxX + entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f), new Vector3d(posX + bb.maxX - bb.minX - entity.width / 2.0f, posY + bb.maxY - bb.minY, posZ + bb.maxZ - bb.minZ - entity.width / 2.0f)};
                    GLUProjection.Projection result;
                    final Vector4f transformed = new Vector4f(scaledResolution.getScaledWidth() * 2.0f, scaledResolution.getScaledHeight() * 2.0f, -1.0f, -1.0f);
                    for (Vector3d vec : corners) {
                        result = GLUProjection.getInstance().project(vec.x - mc.getRenderManager().viewerPosX, vec.y - mc.getRenderManager().viewerPosY, vec.z - mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                        transformed.setX((float) Math.min(transformed.getX(), result.getX()));
                        transformed.setY((float) Math.min(transformed.getY(), result.getY()));
                        transformed.setW((float) Math.max(transformed.getW(), result.getX()));
                        transformed.setZ((float) Math.max(transformed.getZ(), result.getY()));
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.scale(.5f, .5f, .5f);
                    final float x = transformed.x * 2;
                    final float w = (transformed.w * 2) - x;
                    final float y = transformed.y * 2;
                    final NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(ent.getUniqueID());
                    final String p = String.format("%sms", Objects.isNull(networkPlayerInfo) ? "0" : networkPlayerInfo.getResponseTime());
                    final ChatFormatting healthColor = (Math.min((int) ent.getHealth() + (int) ent.getAbsorptionAmount(), 20) >= ent.getMaxHealth() / 1.45f ?
                            ChatFormatting.GREEN : Math.min((int) ent.getHealth() + (int) ent.getAbsorptionAmount(), 20) >= ent.getMaxHealth() / 2f ?
                            ChatFormatting.YELLOW : Math.min((int) ent.getHealth() + (int) ent.getAbsorptionAmount(), 20) >= ent.getMaxHealth() / 3f ? ChatFormatting.RED : ChatFormatting.DARK_RED);
                    final String str = (ping ? " " + ChatFormatting.BLUE + p + " " : "") + healthColor + " " + ((int) ent.getHealth() + (int) ent.getAbsorptionAmount());
                    final String entName = getName(ent) + str;
                    RenderUtil.drawRect((x + (w / 2) -
                                    (RenderUtil.getStringWidth(entName) / 2)) - 1, y - 5 - RenderUtil.getStringHeight(),
                            RenderUtil.getStringWidth(entName) + 2,
                            RenderUtil.getStringHeight() + 3, 0x60000000);
                    mc.fontRenderer.drawStringWithShadow(entName, (x + (w / 2) -
                                    (RenderUtil.getStringWidth(entName) / 2)),
                            y - 3 - RenderUtil.getStringHeight(), clr.getRGB());
                    if (armor && ent instanceof EntityPlayer)
                        drawArmor((EntityPlayer) ent, (int) (x + w / 2), (int) (y - 1 - (RenderUtil.getStringHeight() * 3.15)));
                    GlStateManager.scale(1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }
            }
        });
    }

    private void drawArmor(EntityPlayer player, int x, int y) {
        if (!player.inventory.armorInventory.isEmpty()) {
            final List<ItemStack> items = new ArrayList<>();
            if (player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY)
                items.add(player.getHeldItem(EnumHand.OFF_HAND));
            if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY)
                items.add(player.getHeldItem(EnumHand.MAIN_HAND));
            for (int index = 3; index >= 0; index--) {
                final ItemStack stack = player.inventory.armorInventory.get(index);
                if (stack != ItemStack.EMPTY)
                    items.add(stack);
            }

            int armorX = x - ((items.size() * 19) / 2);
            for (ItemStack stack : items) {
                GlStateManager.pushMatrix();
                GlStateManager.enableLighting();
                mc.getRenderItem().renderItemIntoGUI(stack, armorX, y);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, armorX, y, "");
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                GlStateManager.disableDepth();
                if (stack.isStackable() && stack.getCount() > 0)
                    mc.fontRenderer.drawStringWithShadow(String.valueOf(stack.getCount()), armorX + 4, y + 8, 0xDDD1E6);
                final NBTTagList enchants = stack.getEnchantmentTagList();
                GlStateManager.pushMatrix();
                if (stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 1)
                    mc.fontRenderer.drawStringWithShadow("op", armorX, y, 0xFFFF0000);
                int ency = y + 4;
                if (!enchants.hasNoTags()) {
                    for (NBTBase nbtBase : enchants) {
                        if (nbtBase.getId() == 10) {
                            final NBTTagCompound nbtTagCompound = (NBTTagCompound) nbtBase;
                            final short id = nbtTagCompound.getShort("id");
                            final short level = nbtTagCompound.getShort("lvl");
                            final Enchantment enc = Enchantment.getEnchantmentByID(id);

                            if (enc != null) {
                                final String encName = cachedEnchantmentMap.get(enc.getName()) + level;
                                mc.fontRenderer.drawStringWithShadow(encName, armorX + 4, ency, enc.isCurse() ? 0xff9999 : 0xDDD1E6);
                                ency -= 8;
                            }
                        }
                    }
                }

                if (stack.isItemStackDamageable()) {
                    final float green = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
                    final float red = 1.0f - green;
                    final int dmg = 100 - (int) (red * 100.0f);
                    mc.fontRenderer.drawStringWithShadow(dmg + "",
                            armorX + 4, ency, new Color(MathUtil.clamp((int) (red * 255.0f), 0, 255),
                                    MathUtil.clamp((int) (green * 255.0f), 0, 255), 0).getRGB());
                }

                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
                armorX += 19;
            }
        }
    }

    private void renderItemOverlayIntoGUI(ItemStack stack, int xPosition, int yPosition, String text) {
        if (!stack.isEmpty()) {
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                mc.fontRenderer.drawStringWithShadow(s, (xPosition + 19 - 2 - RenderUtil.getStringWidth(s)), (float) (yPosition + RenderUtil.getStringHeight()), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float) health * 13.0F);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, rgbfordisplay >> 16 & 255, rgbfordisplay >> 8 & 255, rgbfordisplay & 255, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }

            EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
            float f3 = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
            if (f3 > 0.0F) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

    }

    private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    private String getName(EntityLivingBase entityLivingBase) {
        final String rankTitle = rank ? IngrosWare.INSTANCE.getProfileManager().getRank(entityLivingBase.getUniqueID()) != null ?
                "[" + IngrosWare.INSTANCE.getProfileManager().getRank(entityLivingBase.getUniqueID()).getLabel() + "]" : "" : "";
        final String name = IngrosWare.INSTANCE.getFriendManager().get(entityLivingBase.getUniqueID()).map(Friend::getName).orElse(entityLivingBase.getName());

        return String.format("%s %s", rankTitle, name);
    }

    private boolean isValid(EntityLivingBase entity) {
        return mc.player != entity && entity.getEntityId() != -1488 &&
                isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles);
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players && entity instanceof EntityPlayer) || ((monsters && (entity instanceof EntityMob || entity instanceof EntitySlime)) ||
                (passives && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals && entity instanceof IAnimals));
    }

    private Color getEntityColor(Entity entity) {
        return new Color(entity instanceof EntityPlayer && IngrosWare.INSTANCE.getFriendManager().get(entity.getUniqueID()).isPresent()
                ? 0xff2020ff : (entity.isSneaking() ? 0xffffff00 :
                IngrosWare.INSTANCE.getProfileManager().getRank(entity.getUniqueID()) != null ?
                        IngrosWare.INSTANCE.getProfileManager().getRank(entity.getUniqueID()).getColor().getRGB() : playerColor.getRGB()));
    }
}
