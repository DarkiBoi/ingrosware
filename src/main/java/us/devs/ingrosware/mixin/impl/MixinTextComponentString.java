package us.devs.ingrosware.mixin.impl;

import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.devs.ingrosware.mixin.accessors.ITextComponentString;

@Mixin(TextComponentString.class)
public abstract class MixinTextComponentString implements ITextComponentString {

    @Accessor
    @Override
    public abstract void setText(String text);
}