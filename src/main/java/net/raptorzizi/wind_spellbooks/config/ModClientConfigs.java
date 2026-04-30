package net.raptorzizi.wind_spellbooks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        SPEC = BUILDER.build();
    }
}
