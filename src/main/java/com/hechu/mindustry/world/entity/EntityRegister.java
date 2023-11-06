package com.hechu.mindustry.world.entity;

import com.hechu.mindustry.Static;
import com.hechu.mindustry.world.entity.turrets.Duo;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Static.MOD_ID);
    public static final RegistryObject<EntityType<Duo>> DUO = ENTITIES.register("duo", () ->
            EntityType.Builder.of(Duo::new, MobCategory.MISC)
                    .sized(0.5F, 0.8F)
                    .build("duo"));

    public static final  RegistryObject<EntityType<Turret>> TURRET = ENTITIES.register("turret",() ->
            EntityType.Builder.of(Turret::new,MobCategory.MISC)
                    .sized(0.5f,0.8f)
                    .build("turret"));
}
