package best.reich.ingrosware.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;
import best.reich.ingrosware.IngrosWare;
import best.reich.ingrosware.event.impl.other.EventCape;
import best.reich.ingrosware.manager.impl.AbstractListManager;

import java.awt.image.BufferedImage;
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
 * **/
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

    /**
     * Copied idea from how Seppuku does it lol
     * @param event
     */
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
            for(Profile profile : getList()) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    downloadProfileCape(profile);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void downloadProfileCape(Profile profile) {
        if(Minecraft.getMinecraft().getTextureManager() != null) {
            final ResourceLocation cape = getResource(profile.getCapeLocation());

            if(cape == null) {
                final ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, String.format("https://reich.best/capes/%s", profile.getCapeLocation()),
                        null, new IImageBuffer() {

                    @Override
                    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                        return bufferedImage;
                    }

                    @Override
                    public void skinAvailable() {}
                });
                final ResourceLocation resourceLocation = new ResourceLocation(String.format("ingros/capes/%s.png", profile.getUuid()));
                Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, threadDownloadImageData);

                if (resourceLocation != null)
                    CAPE_CACHE.put(profile.getCapeLocation(), resourceLocation);
            }
        }
    }

    private void loadProfiles() {
        try {
            final URL url = new URL(String.format("%s/profiles.json", "https://reich.best/"));
            final URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/4.76");
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
