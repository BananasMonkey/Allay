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
public interface BlockMudBrickDoubleSlab extends Block {
    BlockType<BlockMudBrickDoubleSlab> TYPE = BlockTypeBuilder
            .builder(BlockMudBrickDoubleSlab.class)
            .vanillaBlock(VanillaBlockId.MUD_BRICK_DOUBLE_SLAB)
            .property(VanillaBlockPropertyTypes.TOP_SLOT_BIT)
            .build();
}