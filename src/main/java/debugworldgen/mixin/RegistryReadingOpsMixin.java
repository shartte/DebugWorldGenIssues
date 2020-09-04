package debugworldgen.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryReadingOps.class)
public class RegistryReadingOpsMixin<T> {

    @Inject(method = "encodeOrId", at = @At(value = "RETURN", ordinal = 1))
    protected <E> void encodeOrId(E input, T prefix, RegistryKey<? extends Registry<E>> registryReference, Codec<E> codec, CallbackInfoReturnable<DataResult<T>> cri) {
        if (codec == ConfiguredFeature.CODEC) {
            System.err.println(" *** FAILED TO ENCODE " + input.toString() + " AS ID:" + cri.getReturnValue());
        }
    }

}
