package best.reich.ingrosware.setting;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public interface Setting<V> {
    V getValue();
    void setValue(V value);
    void setValue(String value);
}
