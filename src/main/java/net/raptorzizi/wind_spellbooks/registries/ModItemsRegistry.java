package net.raptorzizi.wind_spellbooks.registries;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.item.armor.AeromancerArmorItem;
import net.raptorzizi.wind_spellbooks.item.weapons.ModStaffTier;

import java.util.function.Supplier;

public class ModItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WindSpellbooksMod.MOD_ID);

    /**
     * Spell items
     */
    public static final DeferredHolder<Item, Item> WIND_STAFF = ITEMS.register("wind_staff", () -> new StaffItem(ItemPropertiesHelper.equipment(1).attributes(ExtendedSwordItem.createAttributes(ModStaffTier.WIND_STAFF)).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> WIND_SPELL_BOOK = ITEMS.register("wind_spell_book", () -> new SpellBook(10)
            .withSpellbookAttributes(new AttributeContainer(ModAttributeRegistry.WIND_SPELL_POWER, .10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)));
    /**
     * Upgrade Orbs
     */
    public static final DeferredHolder<Item, Item> WIND_UPGRADE_ORB = ITEMS.register("wind_upgrade_orb", () -> new UpgradeOrbItem(ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON).component(ComponentRegistry.UPGRADE_ORB_TYPE, ModUpgradeOrbTypeRegistry.WIND_SPELL_POWER)));

    /**
     * Generic Items
     */
    public static final DeferredHolder<Item, Item> WIND_RUNE = ITEMS.register("wind_rune", () -> new Item(ItemPropertiesHelper.material()));
    /**
     * Armor
     */
    public static final DeferredHolder<Item, Item> AEROMANCER_HELMET = ITEMS.register("aeromancer_helmet", () -> new AeromancerArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredHolder<Item, Item> AEROMANCER_CHESTPLATE = ITEMS.register("aeromancer_chestplate", () -> new AeromancerArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredHolder<Item, Item> AEROMANCER_LEGGINGS = ITEMS.register("aeromancer_leggings", () -> new AeromancerArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredHolder<Item, Item> AEROMANCER_BOOTS = ITEMS.register("aeromancer_boots", () -> new AeromancerArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1).durability(ArmorItem.Type.BOOTS.getDurability(37))));

    /**
     * Curios
     */

    /**
     * Music Discs
     */

    /**
     * Spawn eggs
     */
    public static final Supplier<DeferredSpawnEggItem> AEROMANCER_SPAWN_EGG = ITEMS.register("aeromancer_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.AEROMANCER, 0xd0d0d0, 0x979797, ItemPropertiesHelper.material().stacksTo(64)));

    public static void register(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
