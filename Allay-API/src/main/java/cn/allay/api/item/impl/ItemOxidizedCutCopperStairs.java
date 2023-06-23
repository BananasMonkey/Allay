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
public interface ItemOxidizedCutCopperStairs extends ItemStack {
  ItemType<ItemOxidizedCutCopperStairs> TYPE = ItemTypeBuilder
          .builder(ItemOxidizedCutCopperStairs.class)
          .vanillaItem(VanillaItemId.OXIDIZED_CUT_COPPER_STAIRS, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
