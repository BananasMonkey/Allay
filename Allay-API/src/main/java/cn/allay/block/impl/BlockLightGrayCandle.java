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
public interface BlockLightGrayCandle extends Block {
    BlockType<BlockLightGrayCandle> TYPE = BlockTypeBuilder
            .builder(BlockLightGrayCandle.class)
            .vanillaBlock(VanillaBlockId.LIGHT_GRAY_CANDLE)
            .property(VanillaBlockPropertyTypes.CANDLES,
                    VanillaBlockPropertyTypes.LIT)
            .build();
}