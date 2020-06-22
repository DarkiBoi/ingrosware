package best.reich.ingrosware.event.impl.other;

import net.minecraft.client.settings.GameSettings;
import tcb.bces.event.EventCancellable;

public class SetGameOptionEvent extends EventCancellable {

    private final GameSettings.Options settingOption;
    private final int value;

    public SetGameOptionEvent(GameSettings.Options settingOption, int value) {
        this.settingOption = settingOption;
        this.value = value;
    }

    public GameSettings.Options getSettingOption() {
        return settingOption;
    }

    public int getValue() {
        return value;
    }


}
