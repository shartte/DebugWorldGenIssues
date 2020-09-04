package debugworldgen.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(BiomeArray.class)
public class BiomeArrayMixin {

    @Shadow
    private IndexedIterable<Biome> field_25831;

    @Shadow
    private Biome[] data;

    @Inject(method = "toIntArray", at = @At("RETURN"))
    public void toIntArray(CallbackInfoReturnable<int[]> cri) {
        int[] result = cri.getReturnValue();

        // See if any value is -1
        boolean valid = true;
        for (int i : result) {
            if (i == -1) {
                valid = false;
                break;
            }
        }

        if (valid) {
            return;
        }

        Set<Biome> borkedBiomes = new HashSet<>();

        for (int i = 0; i < this.data.length; ++i) {
            Biome biome = this.data[i];
            if (this.field_25831.getRawId(biome) == -1) {
                borkedBiomes.add(biome);
            }
        }

        System.out.println("BORKED BIOMES:");
        System.out.println("-------------------------------------");
        for (Biome borkedBiome : borkedBiomes) {
            // See if we get lucky...
            Identifier borkedId = BuiltinRegistries.BIOME.getId(borkedBiome);
            JsonElement json = Biome.CODEC.encodeStart(JsonOps.INSTANCE, borkedBiome).get().left().orElse(null);
            System.out.println(" - Potential ID: " + borkedBiome + "");
            System.out.println(" - JSON:");
            System.out.println(json);
            System.out.println();
        }
        System.out.println("-------------------------------------");
    }

}
