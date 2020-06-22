package best.reich.ingrosware.traits;

import com.google.gson.JsonObject;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public interface Configable {
    JsonObject toJson();

    void fromJson(JsonObject source);
}
