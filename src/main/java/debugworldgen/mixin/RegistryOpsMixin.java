package debugworldgen.mixin;

import com.mojang.serialization.DynamicOps;
import debugworldgen.impl.Validation;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryOps.class)
public class RegistryOpsMixin {

    @Inject(method = "of", at = @At("RETURN"), require = 1)
    private static <T> void onCreate(DynamicOps<T> delegate, ResourceManager resourceManager, DynamicRegistryManager.Impl dynMgr, CallbackInfoReturnable<RegistryOps<T>> cri) {
        Validation.validate(dynMgr);
    }


}
