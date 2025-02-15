package com.hechu.mindustry.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.MinecraftForge

object MultiBlockRegistry {
    private val registry: MutableMap<ResourceLocation, MultiBlock> = hashMapOf()

    // 注册多方块结构
    @JvmStatic
    fun register(id: ResourceLocation, structure: MultiBlock) {
        registry.putIfAbsent(id, structure)
        MinecraftForge.EVENT_BUS.register(structure) // 注册事件监听
    }

    // 获取所有注册结构
    @JvmStatic
    fun getAllStructures(): List<MultiBlock> {
        return registry.values.toList()
    }

    // 根据控制器方块查询结构
    @JvmStatic
    fun getByController(state: BlockState, pos: BlockPos): MultiBlock? {
        return registry.values.firstOrNull { it.isControllerBlock(state, pos) }
    }
}
