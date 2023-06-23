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
public interface ItemCobbledDeepslateWall extends ItemStack {
  ItemType<ItemCobbledDeepslateWall> TYPE = ItemTypeBuilder
          .builder(ItemCobbledDeepslateWall.class)
          .vanillaItem(VanillaItemId.COBBLED_DEEPSLATE_WALL, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
