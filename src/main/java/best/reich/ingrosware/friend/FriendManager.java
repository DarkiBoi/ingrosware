package best.reich.ingrosware.friend;

import best.reich.ingrosware.manager.impl.AbstractListManager;
import best.reich.ingrosware.traits.Closeable;
import best.reich.ingrosware.traits.Startable;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FriendManager extends AbstractListManager<Friend> implements Startable, Closeable {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private File friendFile;

    public FriendManager(File baseDir) {
        this.friendFile = new File(baseDir, "friends.json");
    }

    @Override
    public void start() {
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
                this.save();
                return;
            }

            this.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() {
        save();
    }

    public void save() {
        try {
            final PrintWriter printWriter = new PrintWriter(friendFile);
            final JsonObject jsonObject = new JsonObject();
            this.getList().forEach(friend -> jsonObject.addProperty(friend.getName(), friend.getUUID().toString()));
            printWriter.print(GSON.toJson(jsonObject));
            printWriter.close();
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void load() {
        try {
            final JsonObject jsonObject = new JsonParser().parse(new FileReader(friendFile)).getAsJsonObject();
            final Set<Map.Entry<String, JsonElement>> elements = jsonObject.entrySet();
            elements.forEach(entry -> add(new Friend(UUID.fromString(entry.getValue().getAsString()), entry.getKey())));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void add(UUID uuid, String name) {
        add(new Friend(uuid, name));
        this.save();
    }

    public Optional<Friend> get(String name) {
        return this.getList().stream().filter(friend -> friend.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Friend> get(UUID uuid) {
        return this.getList().stream().filter(friend -> friend.getUUID().equals(uuid)).findFirst();
    }

    public boolean isFriend(UUID uuid) {
        return this.getList().stream().anyMatch(friend -> friend.getUUID().equals(uuid));
    }

}
