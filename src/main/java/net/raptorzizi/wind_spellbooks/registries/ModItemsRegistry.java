package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.item.armor.AeromancerArmorItem;

import java.util.Map;
import java.util.UUID;

public class ModItemsRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WindSpellbooksMod.MOD_ID);

    /**
     * Spell items
     */
    public static final RegistryObject<Item> WIND_STAFF = ITEMS.register("wind_staff", () ->
            new StaffItem(
                    ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE),
                    4, -3,
                    Map.of(
                            AttributeRegistry.SPELL_POWER.get(),
                            new AttributeModifier(UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567891"), "Weapon modifier", .05, AttributeModifier.Operation.MULTIPLY_BASE),
                            ModAttributeRegistry.WIND_SPELL_POWER.get(),
                            new AttributeModifier(UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567892"), "Weapon modifier", .15, AttributeModifier.Operation.MULTIPLY_BASE),
                            AttributeRegistry.CASTING_MOVESPEED.get(),
                            new AttributeModifier(UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567893"), "Weapon modifier", .25, AttributeModifier.Operation.MULTIPLY_BASE)
                    )
            )
    );

    public static final RegistryObject<Item> WIND_SPELL_BOOK = ITEMS.register("wind_spell_book", () ->
            new SimpleAttributeSpellBook(10, SpellRarity.UNCOMMON,
                    ModAttributeRegistry.WIND_SPELL_POWER.get(), 0.15, 200.0));

    /**
     * Upgrade Orbs
     */
    public static final ResourceKey<UpgradeOrbType> WIND_POWER_ORB_KEY = ResourceKey.create(
            UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY,
            new ResourceLocation(WindSpellbooksMod.MOD_ID, "wind_power"));

    public static final RegistryObject<Item> WIND_UPGRADE_ORB = ITEMS.register("wind_upgrade_orb", () ->
            new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON), WIND_POWER_ORB_KEY));

    /**
     * Generic Items
     */
    public static final RegistryObject<Item> WIND_RUNE = ITEMS.register("wind_rune", () ->
            new Item(ItemPropertiesHelper.material()));

    /**
     * Armor
     */
    public static final RegistryObject<Item> AEROMANCER_HELMET = ITEMS.register("aeromancer_helmet", () ->
            new AeromancerArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1)));
    public static final RegistryObject<Item> AEROMANCER_CHESTPLATE = ITEMS.register("aeromancer_chestplate", () ->
            new AeromancerArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1)));
    public static final RegistryObject<Item> AEROMANCER_LEGGINGS = ITEMS.register("aeromancer_leggings", () ->
            new AeromancerArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1)));
    public static final RegistryObject<Item> AEROMANCER_BOOTS = ITEMS.register("aeromancer_boots", () ->
            new AeromancerArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1)));

    /**
     * Spawn eggs
     */
    public static final RegistryObject<Item> AEROMANCER_SPAWN_EGG = ITEMS.register("aeromancer_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntityRegistry.AEROMANCER, 0xd0d0d0, 0x979797, ItemPropertiesHelper.material().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
