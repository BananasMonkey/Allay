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
public interface ItemPolishedDeepslateDoubleSlab extends ItemStack {
  ItemType<ItemPolishedDeepslateDoubleSlab> TYPE = ItemTypeBuilder
          .builder(ItemPolishedDeepslateDoubleSlab.class)
          .vanillaItem(VanillaItemId.POLISHED_DEEPSLATE_DOUBLE_SLAB, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
