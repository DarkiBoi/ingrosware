package best.reich.ingrosware.friend;

import best.reich.ingrosware.util.other.ProfileHelper;

import java.util.UUID;

/**
 * @author oHare
 */
public class Friend {
    private UUID uuid;
    private String name;
    private String playerName;

    public Friend(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;

        this.playerName = ProfileHelper.INSTANCE.getName(uuid);

        if (this.playerName == null)
            this.playerName = name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
