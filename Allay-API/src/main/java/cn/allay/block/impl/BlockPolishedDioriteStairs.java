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
public interface BlockPolishedDioriteStairs extends Block {
    BlockType<BlockPolishedDioriteStairs> TYPE = BlockTypeBuilder
            .builder(BlockPolishedDioriteStairs.class)
            .vanillaBlock(VanillaBlockId.POLISHED_DIORITE_STAIRS)
            .property(VanillaBlockPropertyTypes.UPSIDE_DOWN_BIT,
                    VanillaBlockPropertyTypes.WEIRDO_DIRECTION)
            .build();
}