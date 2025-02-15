package com.hechu.mindustry.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BedBlock
import net.minecraft.world.level.block.state.properties.DirectionProperty

object DirectionUtils {
    @JvmStatic
    fun getDirection(pos: BlockPos, level: Level): Direction {
        val state = level.getBlockState(pos)

        if (state.block is BedBlock) {
            return BedBlock.getConnectedDirection(state)
        }

        state.properties.forEach {
            if (it is DirectionProperty && state.hasProperty(it)) {
                return state.getValue(it)
            }
        }

        return Direction.NORTH
    }

    @JvmStatic
    @Suppress("MagicNumber")
    fun debugDrawDirection(level: Level, pos: BlockPos, dir: Direction) {
        if (level.isClientSide) {
            for (i in 1..5) {
                val linePos = pos.relative(dir, i)
                level.addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    linePos.x + 0.5,
                    linePos.y + 0.5,
                    linePos.z + 0.5,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }
}
