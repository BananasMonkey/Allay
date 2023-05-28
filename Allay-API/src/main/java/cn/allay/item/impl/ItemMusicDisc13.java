package cn.allay.item.impl;

import cn.allay.item.ItemStack;
import cn.allay.item.data.VanillaItemId;
import cn.allay.item.type.ItemType;
import cn.allay.item.type.ItemTypeBuilder;
import cn.allay.item.type.ItemTypeRegistry;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemMusicDisc13 extends ItemStack {
    ItemType<ItemMusicDisc13> TYPE = ItemTypeBuilder
            .builder(ItemMusicDisc13.class)
            .vanillaItem(VanillaItemId.MUSIC_DISC_13, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}