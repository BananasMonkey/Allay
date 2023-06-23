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
public interface ItemDriedKelpBlock extends ItemStack {
  ItemType<ItemDriedKelpBlock> TYPE = ItemTypeBuilder
          .builder(ItemDriedKelpBlock.class)
          .vanillaItem(VanillaItemId.DRIED_KELP_BLOCK, true)
          .addBasicComponents()
          .build().register(ItemTypeRegistry.getRegistry());
}
