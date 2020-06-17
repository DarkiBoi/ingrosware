package us.devs.ingrosware.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.event.impl.other.EventCape;
import us.devs.ingrosware.manager.impl.AbstractListManager;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * made for Ingros
 *
 * @author Brennan / Seth
 * @since 6/17/2020
 *
 * I looked to seppuku for inspiration because i had no idea how i was finna do this
 **/
public class ProfileManager extends AbstractListManager<Profile> implements IListener {
    private final Map<String, ResourceLocation> CAPE_CACHE = new HashMap<>();

    @Override
    public void start() {
        loadProfiles();
        downloadCapes();

        IngrosWare.INSTANCE.getBus().register(this);
    }

    @Override
    public void close() {
        clear();
        CAPE_CACHE.clear();

        IngrosWare.INSTANCE.getBus().unregister(this);
    }

    @Subscribe
    public void onCape(EventCape event) {
        if(hasCape(event.getPlayer().getUniqueID())) {
            final ResourceLocation capeResource = getCape(event.getPlayer().getUniqueID());

            if(capeResource != null)
                event.setResourceLocation(capeResource);
                event.setCancelled(true);
        }
    }

    private void downloadCapes() {
        try {
            if(Minecraft.getMinecraft().getTextureManager() != null) {
                for(Profile profile : getList()) {
                    final ResourceLocation cape = getResource(profile.getCapeLocation());

                    if(cape == null) {
                        final DynamicTexture texture = new DynamicTexture(ImageIO.read(new URL("http://reich.best/capes/" + profile.getCapeLocation())));

                        if (texture != null) {
                            final ResourceLocation resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(
                                    "ingros/capes", texture);

                            if (resourceLocation != null)
                                CAPE_CACHE.put(profile.getCapeLocation(), resourceLocation);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfiles() {
        try {
            final URL url = new URL("http://reich.best/profiles.json");
            final URLConnection urlConnection = url.openConnection();
            final JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) urlConnection.getContent()));
            final JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(JsonElement element : jsonArray) {
                final JsonObject object = element.getAsJsonObject();
                add(new Profile(UUID.fromString(object.get("uuid").getAsString()), object.get("rank").getAsString(), object.get("cape_location").getAsString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ResourceLocation getResource(String cape) {
        return CAPE_CACHE.get(cape);
    }

    private ResourceLocation getCape(UUID uuid) {
        final Profile profile = get(uuid);
        if(profile != null) {
            final ResourceLocation resourceLocation = getResource(profile.getCapeLocation());
            if(resourceLocation != null)
                return resourceLocation;
        }

        return null;
    }

    private boolean hasCape(UUID uuid) {
        final Profile profile = get(uuid);

        return profile != null;
    }

    public Ranks getRank(UUID uuid) {
        final Profile profile = get(uuid);
        Ranks rank = null;
        if(profile != null) {
            switch (profile.getRank()) {
                case "Developer":
                    rank = Ranks.DEVELOPER;
                    break;
                case "Beta":
                    rank = Ranks.BETA;
                    break;
            }
        }

        return rank;
    }

    public Profile get(UUID uuid) {
        for(Profile profile : getList()) {
            if(profile.getUuid().equals(uuid))
                return profile;
        }

        return null;
    }

    @Override
    public boolean isEnabled() {
        return Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null;
    }
}
