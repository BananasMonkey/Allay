package cn.allay.api.item.interfaces.wall;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemPolishedBlackstoneWallStack extends ItemStack {
  ItemType<ItemPolishedBlackstoneWallStack> POLISHED_BLACKSTONE_WALL_TYPE = ItemTypeBuilder
          .builder(ItemPolishedBlackstoneWallStack.class)
          .vanillaItem(VanillaItemId.POLISHED_BLACKSTONE_WALL)
          .build();
}
