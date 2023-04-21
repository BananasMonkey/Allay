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
public interface BlockBlackstoneDoubleSlab extends Block {
    BlockType<BlockBlackstoneDoubleSlab> TYPE = BlockTypeBuilder
            .builder(BlockBlackstoneDoubleSlab.class)
            .vanillaBlock(VanillaBlockId.BLACKSTONE_DOUBLE_SLAB)
            .property(VanillaBlockPropertyTypes.TOP_SLOT_BIT)
            .build();
}