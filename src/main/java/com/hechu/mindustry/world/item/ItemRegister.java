package com.hechu.mindustry.world.item;

import com.hechu.mindustry.Mindustry;
import com.hechu.mindustry.world.level.block.BlockRegister;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    // Create a Deferred Register to hold Items which will all be registered under the "mindustry" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mindustry.MODID);
    public static final RegistryObject<Item> MECHANICAL_DRILL_ITEM = ITEMS.register(MechanicalDrill.NAME,
            () -> new MechanicalDrill(BlockRegister.MECHANICAL_DRILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> PNEUMATIC_DRILL_ITEM = ITEMS.register(PneumaticDrill.NAME,
            () -> new PneumaticDrill(BlockRegister.PNEUMATIC_DRILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> HEALTH_TEST_ITEM = ITEMS.register(HealthTest.NAME,
            () -> new HealthTest(BlockRegister.HEALTH_TEST.get(), new Item.Properties()));
    public static final RegistryObject<Item> TURRET_ITEM = ITEMS.register(Turret.NAME,
            () -> new Turret(BlockRegister.TURRET.get(), new Item.Properties()));
}
