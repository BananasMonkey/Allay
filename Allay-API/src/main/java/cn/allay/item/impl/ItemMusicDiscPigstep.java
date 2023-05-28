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
public interface ItemMusicDiscPigstep extends ItemStack {
    ItemType<ItemMusicDiscPigstep> TYPE = ItemTypeBuilder
            .builder(ItemMusicDiscPigstep.class)
            .vanillaItem(VanillaItemId.MUSIC_DISC_PIGSTEP, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}