package best.reich.ingrosware.profile;

import java.util.UUID;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/17/2020
 **/
public class Profile {
    private final UUID uuid;
    private final String rank, capeLocation;

    public Profile(UUID uuid, String rank, String capeLocation) {
        this.uuid = uuid;
        this.rank = rank;
        this.capeLocation = capeLocation;
    }

    public String getRank() {
        return rank;
    }

    public String getCapeLocation() {
        return capeLocation;
    }

    public UUID getUuid() {
        return uuid;
    }
}
