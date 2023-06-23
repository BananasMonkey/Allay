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
public interface ItemElement116 extends ItemStack {
  ItemType<ItemElement116> TYPE = ItemTypeBuilder
          .builder(ItemElement116.class)
          .vanillaItem(VanillaItemId.ELEMENT_116, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
