package cn.allay.api.item.interfaces.wood;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemStrippedMangroveWoodStack extends ItemStack {
  ItemType<ItemStrippedMangroveWoodStack> STRIPPED_MANGROVE_WOOD_TYPE = ItemTypeBuilder
          .builder(ItemStrippedMangroveWoodStack.class)
          .vanillaItem(VanillaItemId.STRIPPED_MANGROVE_WOOD)
          .build();
}
