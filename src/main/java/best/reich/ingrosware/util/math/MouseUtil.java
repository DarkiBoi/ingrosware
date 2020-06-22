package best.reich.ingrosware.util.math;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class MouseUtil {

    public static boolean mouseWithin(int mouseX, int mouseY, float x, float y, float width, float height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

}
