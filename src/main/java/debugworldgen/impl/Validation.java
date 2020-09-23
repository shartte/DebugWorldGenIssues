package debugworldgen.impl;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;
import java.util.function.Supplier;

public class Validation {

    public static void validate(DynamicRegistryManager dynMgr) {
        System.out.println("VALIDATING FEATURES AFTER DATAPACK WAS APPLIED:");
        System.out.println("--------------------------------------------");

        // Validate registration status of configured features et al
        MutableRegistry<Biome> biomes = dynMgr.get(Registry.BIOME_KEY);
        MutableRegistry<ConfiguredFeature<?, ?>> cfs = dynMgr.get(Registry.CONFIGURED_FEATURE_WORLDGEN);
        biomes.forEach(biome -> {
            boolean printedBiome = false;
            for (List<Supplier<ConfiguredFeature<?, ?>>> stages : biome.getGenerationSettings().getFeatures()) {
                for (Supplier<ConfiguredFeature<?, ?>> stage : stages) {
                    ConfiguredFeature<?, ?> cf = null;
                    String error = null;
                    try {
                      cf = stage.get();
                    } catch (Throwable e) {
                        error = e.toString();
                    }
                    
                    if (cf == null) {
                        if (error == null) {
                            error = " - NULL FEATURE ENTRY FOUND!!!";
                        }
                    } else {
                        Identifier fid = cfs.getId(cf);
                        if (fid == null) {
                            RegistryReadingOps<JsonElement> ops = RegistryReadingOps.of(JsonOps.INSTANCE, dynMgr);
                            DataResult<JsonElement> encodedJson = ConfiguredFeature.CODEC.encodeStart(ops, stage);
                            error = " - UNRESOLVABLE: CONFIGURED " + encodedJson;
                        }
                    }

                    if (error != null) {
                        if (!printedBiome) {
                            System.out.println(biomes.getId(biome));
                            printedBiome = true;
                        }
                        System.out.println(error);
                    }
                }
            }
        });
    }

}
