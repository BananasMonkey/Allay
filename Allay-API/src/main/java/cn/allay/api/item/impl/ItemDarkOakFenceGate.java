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
public interface ItemDarkOakFenceGate extends ItemStack {
  ItemType<ItemDarkOakFenceGate> TYPE = ItemTypeBuilder
          .builder(ItemDarkOakFenceGate.class)
          .vanillaItem(VanillaItemId.DARK_OAK_FENCE_GATE, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
