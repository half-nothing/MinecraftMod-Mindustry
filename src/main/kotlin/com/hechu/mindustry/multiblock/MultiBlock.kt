package com.hechu.mindustry.multiblock

import com.hechu.mindustry.MindustryConstants.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.event.level.BlockEvent.BreakEvent
import net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

private const val CHECK_DELAY: Long = 5L

abstract class MultiBlock(
    protected val controllerBlock: Block
) {
    protected val structureComponents: MutableList<Pair<Block, BlockPos>> = mutableListOf()
    protected val structureBlocks: MutableSet<Block> = mutableSetOf()
    protected lateinit var controllerBlockPos: BlockPos

    fun addComponent(block: Block, relativePos: BlockPos) {
        structureBlocks.add(block)
        structureComponents.add(Pair(block, relativePos))
    }

    fun isControllerBlock(state: BlockState, pos: BlockPos): Boolean =
        state.block == controllerBlock && pos == controllerBlockPos

    fun isComponentBlock(state: BlockState, pos: BlockPos): Boolean {
        // 如果是控制器，返回true
        if (isControllerBlock(state, pos)) {
            return true
        }
        // 如果都不是结构内的方块，则返回false
        if (!structureBlocks.contains(state.block)) {
            return false
        }
        // 计算相对坐标
        val relativePos = pos.subtract(controllerBlockPos)
        return structureComponents.firstOrNull { it.second == relativePos } != null
    }

    @SubscribeEvent
    fun onBlockPlace(event: EntityPlaceEvent) {
        // 如果放置的是控制器
        if (event.placedBlock.block.equals(controllerBlock)) {
            logger.debug("Controller block pos {}", event.pos)
            controllerBlockPos = event.pos
            scheduleStructureCheck(event.level)
        }

        if (!::controllerBlockPos.isInitialized) {
            return
        }

        // 如果放置的方块是多方块结构内的方块
        if (structureBlocks.contains(event.placedBlock.block)) {
            scheduleStructureCheck(event.level)
        }
    }

    @SubscribeEvent
    fun onBlockBreak(event: BreakEvent) {
        if (isComponentBlock(event.state, event.pos)) {
            deactivate(event.level)
        }
    }

    private fun scheduleStructureCheck(level: LevelAccessor) {
        // 使用定时任务进行延迟检测
        CoroutineScope(Dispatchers.Default).launch {
            delay(CHECK_DELAY)
            if (checkStructure(level)) {
                activate(level)
            }
        }
    }

    abstract fun checkStructure(level: LevelAccessor): Boolean

    // 结构激活逻辑
    abstract fun activate(level: LevelAccessor)

    // 结构失效逻辑
    abstract fun deactivate(level: LevelAccessor)
}
