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
public interface BlockMediumAmethystBud extends Block {
    BlockType<BlockMediumAmethystBud> TYPE = BlockTypeBuilder
            .builder(BlockMediumAmethystBud.class)
            .vanillaBlock(VanillaBlockId.MEDIUM_AMETHYST_BUD)
            .property(VanillaBlockPropertyTypes.FACING_DIRECTION)
            .build();
}