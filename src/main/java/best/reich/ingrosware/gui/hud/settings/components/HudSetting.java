package best.reich.ingrosware.gui.hud.settings.components;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class HudSetting {
    private String label;
    public float posX, posY,height;

    public HudSetting(String label, float posX, float posY) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
    }

    public void init() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public String getLabel() {
        return label;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void keyTyped(char typedChar, int key) {
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
