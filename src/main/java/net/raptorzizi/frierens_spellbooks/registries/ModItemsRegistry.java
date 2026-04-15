package net.raptorzizi.frierens_spellbooks.registries;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.raptorzizi.frierens_spellbooks.FrierensSpellbooksMod;
import net.raptorzizi.frierens_spellbooks.item.armor.AeromancerArmorItem;

public class ModItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FrierensSpellbooksMod.MOD_ID);

    /**
     * Spell items
     */

    /**
     * Generic Items
     */

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

    public static void register(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
