package best.reich.ingrosware.mixin;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public class IngrosAccessTransformer extends AccessTransformer {

    public IngrosAccessTransformer() throws IOException {
        super("ingros_at.cfg");
    }
}
