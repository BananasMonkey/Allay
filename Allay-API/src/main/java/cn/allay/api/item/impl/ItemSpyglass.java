package cn.allay.api.item.impl;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;
import cn.allay.api.item.type.ItemTypeRegistry;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemSpyglass extends ItemStack {
  ItemType<ItemSpyglass> TYPE = ItemTypeBuilder
          .builder(ItemSpyglass.class)
          .vanillaItem(VanillaItemId.SPYGLASS, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
