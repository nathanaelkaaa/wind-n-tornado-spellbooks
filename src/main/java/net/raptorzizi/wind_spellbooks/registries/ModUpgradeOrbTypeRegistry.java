package net.raptorzizi.wind_spellbooks.registries;

import net.raptorzizi.wind_spellbooks.item.armor.WindUpgradeType;

public class ModUpgradeOrbTypeRegistry {
    // WindUpgradeType enum registers itself into UpgradeType.UPGRADE_REGISTRY on class load.
    // Reference the enum here to ensure the class is loaded during mod initialization.
    public static void init() {
        WindUpgradeType.values();
    }
}
