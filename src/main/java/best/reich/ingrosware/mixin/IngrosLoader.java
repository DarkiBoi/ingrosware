package best.reich.ingrosware.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class IngrosLoader implements IFMLLoadingPlugin {

    public IngrosLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.ingros.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return IngrosAccessTransformer.class.getName();
    }

}
