package best.reich.ingrosware.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class MathUtil {

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static <T extends Number> T clamp(T value, T minimum, T maximum) {
        return value.floatValue() <= minimum.floatValue() ? minimum : (value.floatValue() >= maximum.floatValue() ? maximum : value);
    }

}
