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
public interface ItemEndermiteSpawnEgg extends ItemStack {
  ItemType<ItemEndermiteSpawnEgg> TYPE = ItemTypeBuilder
          .builder(ItemEndermiteSpawnEgg.class)
          .vanillaItem(VanillaItemId.ENDERMITE_SPAWN_EGG, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
