package best.reich.ingrosware.setting.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * made for IngrosWare-Recode
 *
 * @author oHare
 * @since 6/16/2020
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bind {
    boolean pressed();
}
