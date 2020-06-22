package best.reich.ingrosware.util.other;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/14/2020
 **/
public enum ProfileHelper {
    INSTANCE;

    private static final Map<String, UUID> CACHED_NAMES = new HashMap<>();
    private static final String NAME = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String PROFILE = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

    public UUID getUUID(final String name) {
        if (CACHED_NAMES.containsKey(name)) {
            return CACHED_NAMES.get(name);
        }
        try {
            final Reader uuidReader = new InputStreamReader(
                    new URL(String.format(NAME, name)).openStream());
            final JsonObject jsonObject = new JsonParser().parse(uuidReader).getAsJsonObject();
            String unfomatted = jsonObject.get("id").getAsString();
            String formatted = "";
            for (final int length : new int[] { 8, 4, 4, 4, 12 }) {
                formatted += "-";
                for (int i = 0; i < length; ++i) {
                    formatted += unfomatted.charAt(0);
                    unfomatted = unfomatted.substring(1);
                }
            }
            formatted = formatted.substring(1);
            final UUID uuid = UUID.fromString(formatted);
            CACHED_NAMES.put(name, uuid);
            return uuid;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getName(final UUID uuid) {
        if (CACHED_NAMES.containsValue(uuid)) {
            for(Map.Entry<String, UUID> entry : CACHED_NAMES.entrySet()) {
                if(entry.getValue() == uuid)
                    return entry.getKey();
            }
        }

        try {
            final Reader uuidReader = new InputStreamReader(
                    new URL(String.format(PROFILE,
                            uuid.toString().replaceAll("-", ""))).openStream());
            final JsonElement jsonElement = new JsonParser().parse(uuidReader);
            if(jsonElement instanceof JsonObject) {
                final JsonObject object = jsonElement.getAsJsonObject();
                final String name = object.get("name").getAsString();
                CACHED_NAMES.put(name, uuid);
                return name;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }

        return "";
    }

}
