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
public interface BlockPolishedBlackstoneButton extends Block {
    BlockType<BlockPolishedBlackstoneButton> TYPE = BlockTypeBuilder
            .builder(BlockPolishedBlackstoneButton.class)
            .vanillaBlock(VanillaBlockId.POLISHED_BLACKSTONE_BUTTON)
            .property(VanillaBlockPropertyTypes.BUTTON_PRESSED_BIT,
                    VanillaBlockPropertyTypes.FACING_DIRECTION)
            .build();
}