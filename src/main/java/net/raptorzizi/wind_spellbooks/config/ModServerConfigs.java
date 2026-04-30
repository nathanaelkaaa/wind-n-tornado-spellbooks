package net.raptorzizi.wind_spellbooks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModServerConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> TAVERN_SPAWNRATE;

    static {
        {
            BUILDER.push("Worldgen");
            TAVERN_SPAWNRATE = BUILDER.comment("The weight of the tavern spawning in a village. Default: 4")
                    .define("tavernWeight", 4);
            BUILDER.pop();
        }

        SPEC = BUILDER.build();
    }
}
