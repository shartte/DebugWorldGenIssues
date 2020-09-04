package debugworldgen.mixin;

import debugworldgen.impl.Validation;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DynamicRegistryManager.class, priority = 99999)
public class DynamicRegistryManagerMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"), require = 1)
    private static void onInit(CallbackInfo ci) {
        System.err.println("DynamicRegistryManager is being loaded. Further registrations pointless!");
        Thread.dumpStack();
    }

    @Inject(method = "create", at = @At("RETURN"), require = 1)
    private static void onCreate(CallbackInfoReturnable<DynamicRegistryManager.Impl> cri) {
        Validation.validate(cri.getReturnValue());
    }

}
