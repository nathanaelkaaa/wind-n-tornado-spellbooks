package net.raptorzizi.wind_spellbooks.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.raptorzizi.wind_spellbooks.WindSpellbooksMod;
import net.raptorzizi.wind_spellbooks.entity.mobs.wizards.aeromancer.AeromancerEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.almighty_push.AlmightyPushEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.DashStopEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.IronSlashEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.iron_slash.SlashEffectEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.tornado.TornadoEntity;
import net.raptorzizi.wind_spellbooks.entity.spells.wind_blade.WindBladeProjectile;

public class ModEntityRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WindSpellbooksMod.MOD_ID);

    public static final RegistryObject<EntityType<TornadoEntity>> TORNADO_ENTITY =
            ENTITIES.register("tornado", () ->
                    EntityType.Builder.<TornadoEntity>of(TornadoEntity::new, MobCategory.MISC)
                            .sized(1f, 4f)
                            .clientTrackingRange(10)
                            .updateInterval(3)
                            .build("tornado")
            );

    public static final RegistryObject<EntityType<IronSlashEntity>> IRON_SLASH_ENTITY =
            ENTITIES.register("iron_slash", () ->
                    EntityType.Builder.<IronSlashEntity>of(IronSlashEntity::new, MobCategory.MISC)
                            .sized(1.5f, 2.0f)
                            .clientTrackingRange(64)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "iron_slash").toString())
            );

    public static final RegistryObject<EntityType<DashStopEntity>> DASH_STOP_ENTITY =
            ENTITIES.register("dash_stop", () ->
                    EntityType.Builder.<DashStopEntity>of(DashStopEntity::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .clientTrackingRange(0)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "dash_stop").toString())
            );

    public static final RegistryObject<EntityType<SlashEffectEntity>> SLASH_EFFECT_ENTITY =
            ENTITIES.register("slash_effect", () ->
                    EntityType.Builder.<SlashEffectEntity>of(SlashEffectEntity::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .clientTrackingRange(128)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "slash_effect").toString())
            );

    public static final RegistryObject<EntityType<AlmightyPushEntity>> ALMIGHTY_PUSH_ENTITY =
            ENTITIES.register("almighty_push", () ->
                    EntityType.Builder.<AlmightyPushEntity>of(AlmightyPushEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "almighty_push").toString())
            );

    public static final RegistryObject<EntityType<WindBladeProjectile>> WIND_BLADE_PROJECTILE =
            ENTITIES.register("wind_blade", () ->
                    EntityType.Builder.<WindBladeProjectile>of(WindBladeProjectile::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "wind_blade").toString())
            );

    public static final RegistryObject<EntityType<AeromancerEntity>> AEROMANCER =
            ENTITIES.register("aeromancer", () ->
                    EntityType.Builder.of(AeromancerEntity::new, MobCategory.MONSTER)
                            .sized(.6f, 1.8f)
                            .clientTrackingRange(64)
                            .build(new ResourceLocation(WindSpellbooksMod.MOD_ID, "aeromancer").toString())
            );

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
