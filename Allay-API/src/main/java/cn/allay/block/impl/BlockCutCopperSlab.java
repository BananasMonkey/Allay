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
public interface BlockCutCopperSlab extends Block {
    BlockType<BlockCutCopperSlab> TYPE = BlockTypeBuilder
            .builder(BlockCutCopperSlab.class)
            .vanillaBlock(VanillaBlockId.CUT_COPPER_SLAB)
            .property(VanillaBlockPropertyTypes.TOP_SLOT_BIT)
            .build();
}