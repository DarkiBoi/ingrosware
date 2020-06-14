package us.devs.ingrosware.friend;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import us.devs.ingrosware.IngrosWare;
import us.devs.ingrosware.traits.Closeable;
import us.devs.ingrosware.traits.Startable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FriendManager implements Startable, Closeable {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private ArrayList<Friend> friends = new ArrayList<>();
    private File friendFile;
    private final File baseDir;

    public FriendManager(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void start() {
        friendFile = new File(baseDir + File.separator + "friends.json");
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
                return;
            }
            loadFile();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void close() {
        saveFile();
    }

    private void loadFile() {
        if (!friendFile.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(friendFile)) {
            setFriends(GSON.fromJson(inFile, new TypeToken<ArrayList<Friend>>() {
            }.getType()));

            if (getFriends() == null)
                setFriends(new ArrayList<>());

        } catch (Exception ignored) {
        }
    }

    private void saveFile() {
        if (friendFile.exists()) {
            try (PrintWriter writer = new PrintWriter(friendFile)) {
                writer.print(GSON.toJson(getFriends()));
            } catch (Exception ignored) {
            }
        }
    }

    public void addFriend(String name) {
        getFriends().add(new Friend(name));
    }

    public void addFriendWithAlias(String name, String alias) {
        getFriends().add(new Friend(name, alias));
    }

    public boolean isFriend(String ign) {
        return getFriend(ign) != null;
    }

    public Friend getFriend(String ign) {
        for (Friend friend : getFriends()) {
            if (friend.getName().equalsIgnoreCase(ign)) {
                return friend;
            }
        }
        return null;
    }

    public void removeFriend(String name) {
        Friend f = getFriend(name);
        if (f != null) {
            getFriends().remove(f);
        }
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }
}
