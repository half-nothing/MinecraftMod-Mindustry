package com.hechu.mindustry.creative;

import com.hechu.mindustry.MindustryConstants;
import com.hechu.mindustry.world.item.ItemRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.hechu.mindustry.MindustryConstants.MOD_ID;

public class CreativeModeTabRegister {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MindustryConstants.MOD_ID);
    public static final RegistryObject<CreativeModeTab> MINDUSTRY_CREATIVE_TAB = CREATIVE_MODE_TABS.register("mindustry", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID + ".mindustry"))
            .icon(() -> new ItemStack(ItemRegister.MECHANICAL_DRILL_ITEM.get()))
            .displayItems((featureFlags, output) -> {
                output.accept(ItemRegister.MECHANICAL_DRILL_ITEM.get());
                output.accept(ItemRegister.PNEUMATIC_DRILL_ITEM.get());
                output.accept(ItemRegister.HEALTH_TEST_ITEM.get());
                output.accept(ItemRegister.POWER_NODE_BLOCK_ITEM.get());
            }).build()
    );
}
