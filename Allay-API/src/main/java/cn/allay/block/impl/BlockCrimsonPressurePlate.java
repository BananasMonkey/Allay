package cn.allay.block.impl;

import cn.allay.block.Block;
import cn.allay.block.data.VanillaBlockId;
import cn.allay.block.property.vanilla.VanillaBlockPropertyTypes;
import cn.allay.block.type.BlockType;
import cn.allay.block.type.BlockTypeBuilder;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockCrimsonPressurePlate extends Block {
    BlockType<BlockCrimsonPressurePlate> TYPE = BlockTypeBuilder
            .builder(BlockCrimsonPressurePlate.class)
            .vanillaBlock(VanillaBlockId.CRIMSON_PRESSURE_PLATE)
            .property(VanillaBlockPropertyTypes.REDSTONE_SIGNAL)
            .build();
}