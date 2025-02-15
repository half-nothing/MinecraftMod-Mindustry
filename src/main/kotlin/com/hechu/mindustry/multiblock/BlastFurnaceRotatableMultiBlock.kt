package com.hechu.mindustry.multiblock

import com.hechu.mindustry.MindustryConstants.logger
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Blocks

class BlastFurnaceRotatableMultiBlock :
    MultiBlock(Blocks.BLAST_FURNACE) {
    init {
        addComponent(Blocks.BRICKS, BlockPos(0, 1, 0))
        addComponent(Blocks.GOLD_BLOCK, BlockPos(1, 0, 0))
        addComponent(Blocks.IRON_BLOCK, BlockPos(-1, 0, 0))
    }

    override fun checkStructure(level: LevelAccessor): Boolean {
        logger.debug("Check structure")
        structureComponents.forEach { (block, relatePos) ->
            logger.debug("Check block {}, {}", block, relatePos)
            val pos = controllerBlockPos.offset(relatePos)
            logger.debug("Check pos {}", pos)
            val blockState = level.getBlockState(pos)
            logger.debug("Check block {}", blockState)
            if (blockState.block != block) {
                return false
            }
        }
        return true
    }

    override fun activate(level: LevelAccessor) {
        logger.info("Activating BlastFurnaceRotatable MultiBlock")
    }

    override fun deactivate(level: LevelAccessor) {
        logger.info("Deactivating BlastFurnaceRotatable MultiBlock")
    }
}
