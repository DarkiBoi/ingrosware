package us.devs.ingrosware.hud.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import us.devs.ingrosware.hud.Component;
import us.devs.ingrosware.hud.annotation.ComponentManifest;
import us.devs.ingrosware.util.render.RenderUtil;


/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/19/2020
 **/
@ComponentManifest(label = "Inventory", x = 10, y = 10, width = 144, height = 48)
public class InventoryComponent extends Component {

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        RenderUtil.drawBorderedRect(getX(),
                getY(), getWidth(), getHeight(), 1, 0x75101010, 0x90000000);
        for (int i = 0; i < 27; i++) {
            final ItemStack itemStack = mc.player.inventory.mainInventory.get(i + 9);
            int offsetX = (int) ((this.getX()  + (i % 9) * 16));
            int offsetY = (int) ((this.getY() + (i / 9) * 16));
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
        }
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
