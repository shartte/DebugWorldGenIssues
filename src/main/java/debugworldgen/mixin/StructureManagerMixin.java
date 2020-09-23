package debugworldgen.mixin;

import debugworldgen.impl.CurrentEntry;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(StructureManager.class)
public class StructureManagerMixin {

    @Unique
    private AtomicReference<CurrentEntry> current = new AtomicReference<>();

    private static final Logger LOGGER = LogManager.getLogger();

    @Inject(method = "getStructure", at = @At("HEAD"))
    public void getStructureLock(Identifier id, CallbackInfoReturnable<Structure> cri) {
        CurrentEntry e = new CurrentEntry(id);
        if (!current.compareAndSet(null, e)) {
            CurrentEntry oldE = current.get();
            if (oldE == null) {
                LOGGER.error("CONCURRENT ACCESS TO getStructure: {}, unable to find out which one it was", id);
            } else {
                LOGGER.error("CONCURRENT ACCESS TO getStructure: {} {} (Previous was: {}, {})",
                        id, Thread.currentThread(), oldE.getId(), oldE.getThread());
            }
        }
    }

    @Inject(method = "getStructure", at = @At("TAIL"))
    public void getStructureUnlock(Identifier id, CallbackInfoReturnable<Structure> cri) {
        current.set(null);
    }

}
