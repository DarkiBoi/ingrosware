package us.devs.ingrosware.mixin.impl;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import us.devs.ingrosware.IngrosWare;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/18/2020
 **/
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @Shadow
    private boolean bidiFlag;
    @Shadow
    private float red;
    @Shadow
    private float blue;
    @Shadow
    private float green;
    @Shadow
    private float alpha;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;

    @Shadow
    public abstract String bidiReorder(String p_bidiReorder_1_);
    @Shadow
    public abstract void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_);
    @Shadow
    public abstract void renderStringAtPos(String p_renderStringAtPos_1_, boolean p_renderStringAtPos_2_);

    /**
     * Had to do for custom font usage
     * @param p_renderString_1_
     * @param p_renderString_2_
     * @param p_renderString_3_
     * @param p_renderString_4_
     * @param p_renderString_5_
     * @return
     */
    @Overwrite
    private int renderString(String p_renderString_1_, float p_renderString_2_, float p_renderString_3_, int p_renderString_4_, boolean p_renderString_5_) {
        if (p_renderString_1_ == null) {
            return 0;
        } else {
            if (this.bidiFlag) {
                p_renderString_1_ = this.bidiReorder(p_renderString_1_);
            }

            if ((p_renderString_4_ & -67108864) == 0) {
                p_renderString_4_ |= -16777216;
            }

            if (p_renderString_5_) {
                p_renderString_4_ = (p_renderString_4_ & 16579836) >> 2 | p_renderString_4_ & -16777216;
            }

            this.red = (float)(p_renderString_4_ >> 16 & 255) / 255.0F;
            this.blue = (float)(p_renderString_4_ >> 8 & 255) / 255.0F;
            this.green = (float)(p_renderString_4_ & 255) / 255.0F;
            this.alpha = (float)(p_renderString_4_ >> 24 & 255) / 255.0F;
            this.setColor(this.red, this.blue, this.green, this.alpha);
            this.posX = p_renderString_2_;
            this.posY = p_renderString_3_;
            if(IngrosWare.INSTANCE.getFontManager() != null) {
                if (IngrosWare.INSTANCE.getFontManager().isUsingCustomFont()) {
                    IngrosWare.INSTANCE.getFontManager().getCurrentFont().drawString(p_renderString_1_, p_renderString_2_, p_renderString_3_, p_renderString_4_, p_renderString_5_);
                } else {
                    this.renderStringAtPos(p_renderString_1_, p_renderString_5_);
                }
            } else {
                this.renderStringAtPos(p_renderString_1_, p_renderString_5_);
            }

            return (int)this.posX;
        }
    }

}
