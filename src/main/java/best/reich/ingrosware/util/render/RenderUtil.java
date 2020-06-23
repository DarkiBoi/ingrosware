package best.reich.ingrosware.util.render;

import best.reich.ingrosware.mixin.accessors.IRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import best.reich.ingrosware.IngrosWare;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;


/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class RenderUtil {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private final static Frustum frustrum = new Frustum();

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((sr.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void drawCircle(float x, float y, float r, int c) {
        float f = (c >> 24 & 0xFF) / 255.0f;
        float f2 = (c >> 16 & 0xFF) / 255.0f;
        float f3 = (c >> 8 & 0xFF) / 255.0f;
        float f4 = (c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * Math.PI / 180.0) * (r / 2);
            double y2 = Math.cos(i * Math.PI / 180.0) * (r / 2);
            GL11.glVertex2d(x + r / 2 + x2, y + r / 2 + y2);
        }
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * Math.PI / 180.0) * ((r / 2));
            double y2 = Math.cos(i * Math.PI / 180.0) * ((r / 2));
            GL11.glVertex2d(x + ((r / 2)) + x2, y + ((r / 2)) + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        double ps;
        double cs;
        double i;
        double[] outer;
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.enableBlend();
        GL11.glDisable((int)3553);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        GL11.glBegin((int)9);
        for (i = 0.0; i < 36.0; i += 1.0) {
            cs = i * 10.0 * 3.141592653589793 / 180.0;
            ps = (i * 10.0 - 1.0) * 3.141592653589793 / 180.0;
            outer = new double[]{Math.cos(cs) * (double)radius, (- Math.sin(cs)) * (double)radius, Math.cos(ps) * (double)radius, (- Math.sin(ps)) * (double)radius};
            GL11.glVertex2d((double)((double)x + outer[0]), (double)((double)y + outer[1]));
        }
        GL11.glEnd();
        GL11.glEnable((int)2848);
        GL11.glBegin((int)3);
        for (i = 0.0; i < 37.0; i += 1.0) {
            cs = i * 10.0 * 3.141592653589793 / 180.0;
            ps = (i * 10.0 - 1.0) * 3.141592653589793 / 180.0;
            outer = new double[]{Math.cos(cs) * (double)radius, (- Math.sin(cs)) * (double)radius, Math.cos(ps) * (double)radius, (- Math.sin(ps)) * (double)radius};
            GL11.glVertex2d((double)((double)x + outer[0]), (double)((double)y + outer[1]));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.alphaFunc(516, 0.1f);
    }

    public static void drawBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();
    }

    public static void drawOutlinedBox(AxisAlignedBB bb) {
        GL11.glBegin(GL_LINES);
        {
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        GL11.glEnd();
    }

    public static float getStringHeight() {
        return IngrosWare.INSTANCE.getFontManager().isUsingCustomFont() ? IngrosWare.INSTANCE.getFontManager().getCurrentFont().getHeight() : mc.fontRenderer.FONT_HEIGHT;
    }

    public static float getStringWidth(String text) {
        return IngrosWare.INSTANCE.getFontManager().isUsingCustomFont() ? IngrosWare.INSTANCE.getFontManager().getCurrentFont().getStringWidth(text):mc.fontRenderer.getStringWidth(text);
    }

    public static void drawESPOutline(AxisAlignedBB bb, float red, float green, float blue, float alpha, float width) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        RenderUtil.drawOutlinedBox(bb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static void drawESP(AxisAlignedBB bb, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        RenderUtil.drawBox(bb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static void drawBorderedRect(float x, float y, float width, float height, float lineSize, int color, int borderColor) {
        drawRect(x, y, width, height, color);
        drawRect(x, y, lineSize, height, borderColor);
        drawRect(x, y, width, lineSize, borderColor);
        drawRect(x + width - lineSize, y, lineSize, height, borderColor);
        drawRect(x, y + height - lineSize, width, lineSize, borderColor);

    }

    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(3);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x + width - 6.5, y + 3);
        GL11.glVertex2d(x + width - 11.5, y + 10);
        GL11.glVertex2d(x + width - 13.5, y + 8);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float lvt_5_2_;
        float p_drawRect_2_ = x + w;
        float p_drawRect_3_ = y + h;
        if (x < p_drawRect_2_) {
            lvt_5_2_ = x;
            x = p_drawRect_2_;
            p_drawRect_2_ = lvt_5_2_;
        }

        if (y < p_drawRect_3_) {
            lvt_5_2_ = y;
            y = p_drawRect_3_;
            p_drawRect_3_ = lvt_5_2_;
        }

        float lvt_5_3_ = (float) (color >> 24 & 255) / 255.0F;
        float lvt_6_1_ = (float) (color >> 16 & 255) / 255.0F;
        float lvt_7_1_ = (float) (color >> 8 & 255) / 255.0F;
        float lvt_8_1_ = (float) (color & 255) / 255.0F;
        Tessellator lvt_9_1_ = Tessellator.getInstance();
        BufferBuilder lvt_10_1_ = lvt_9_1_.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(lvt_6_1_, lvt_7_1_, lvt_8_1_, lvt_5_3_);
        lvt_10_1_.begin(7, DefaultVertexFormats.POSITION);
        lvt_10_1_.pos(x, p_drawRect_3_, 0.0D).endVertex();
        lvt_10_1_.pos(p_drawRect_2_, p_drawRect_3_, 0.0D).endVertex();
        lvt_10_1_.pos(p_drawRect_2_, y, 0.0D).endVertex();
        lvt_10_1_.pos(x, y, 0.0D).endVertex();
        lvt_9_1_.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.getRenderViewEntity();
        frustrum.setPosition(Objects.requireNonNull(current).posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void renderTag(String name, double pX, double pY, double pZ, int color) {
        float scale = (float) (mc.player.getDistance(pX + ((IRenderManager) mc.getRenderManager()).getRenderPosX(), pY + ((IRenderManager) mc.getRenderManager()).getRenderPosY(), pZ + ((IRenderManager) mc.getRenderManager()).getRenderPosZ()) / 8.0D);
        if (scale < 1.6F) {
            scale = 1.6F;
        }
        scale /= 50;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        int width = mc.fontRenderer.getStringWidth(name) / 2;
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        mc.fontRenderer.drawStringWithShadow(name, -(width), -(mc.fontRenderer.FONT_HEIGHT + 7), color);
        GL11.glScalef(1f, 1f, 1f);
        GlStateManager.enableTexture2D();
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static void drawRect2(double x, double y, double x2, double y2, int color) {
        Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
    }

    public static void drawBordered(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        drawRect2(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        drawRect2(x, y + 1.0 - fix, x + thickness, y2, outline);
        drawRect2(x, y, x2 - 1.0 + fix, y + thickness, outline);
        drawRect2(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        drawRect2(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 300).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 300).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
