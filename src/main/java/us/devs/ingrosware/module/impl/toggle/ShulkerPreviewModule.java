package us.devs.ingrosware.module.impl.toggle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import org.lwjgl.input.Keyboard;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.event.impl.render.RenderToolTipEvent;
import us.devs.ingrosware.module.ModuleCategory;
import us.devs.ingrosware.module.annotation.Toggleable;
import us.devs.ingrosware.module.types.ToggleableModule;
import us.devs.ingrosware.util.render.RenderUtil;

/**
 * Made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/15/2020
 **/
@Toggleable(label = "ShulkerPreview", category = ModuleCategory.RENDER, color = 0xffAEFFAE, bind = Keyboard.KEY_NONE, hidden = true)
public class ShulkerPreviewModule extends ToggleableModule {

    @Subscribe
    public void onRender(RenderToolTipEvent event) {
        if (mc.world == null || mc.player == null || !mc.player.isEntityAlive() ||event.getStack().getItem() == null) return;
        if (event.getStack().getItem() instanceof ItemShulkerBox) {
            NBTTagCompound tagCompound = event.getStack().getTagCompound();
            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
                if (blockEntityTag.hasKey("Items", 9)) {
                    event.setCancelled(true);
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
                    GlStateManager.enableBlend();
                    GlStateManager.disableRescaleNormal();
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    final int width = Math.max(144, mc.fontRenderer.getStringWidth(event.getStack().getDisplayName()) + 3);
                    final int x1 = event.getX() + 12;
                    final int y1 = event.getY() - 12;
                    final int height = 57;
                    mc.getRenderItem().zLevel = 300.0F;
                    RenderUtil.drawGradientRect(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
                    RenderUtil.drawGradientRect(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
                    RenderUtil.drawGradientRect(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
                    RenderUtil.drawGradientRect(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
                    RenderUtil.drawGradientRect(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
                    RenderUtil.drawGradientRect(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 1347420415, 1344798847);
                    RenderUtil.drawGradientRect(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 1347420415, 1344798847);
                    RenderUtil.drawGradientRect(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 1347420415, 1347420415);
                    RenderUtil.drawGradientRect(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);
                    mc.fontRenderer.drawString(event.getStack().getDisplayName(), event.getX() + 12, event.getY() - 12, 0xffffff);
                    GlStateManager.enableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();
                    for (int i = 0; i < nonnulllist.size(); i++) {
                        final int iX = event.getX() + (i % 9) * 16 + 11;
                        final int iY = event.getY() + (i / 9) * 16 - 11 + 8;
                        ItemStack itemStack = nonnulllist.get(i);

                        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, iX, iY, null);
                    }
                    RenderHelper.disableStandardItemLighting();
                    mc.getRenderItem().zLevel = 0.0F;
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();
                }
            }
        }
    }
}
