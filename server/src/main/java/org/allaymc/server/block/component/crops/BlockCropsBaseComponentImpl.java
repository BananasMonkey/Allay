package org.allaymc.server.block.component.crops;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.server.block.component.BlockBaseComponentImpl;

/**
 * @author Dhaiven
 */
// TODO
public class BlockCropsBaseComponentImpl extends BlockBaseComponentImpl {
    public BlockCropsBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType);
    }

    /*@Override
    public boolean canKeepExisting(BlockStateWithPos current, BlockStateWithPos neighbor, BlockFace face) {
        if (face != BlockFace.DOWN) return true;
        return neighbor.blockState().getBlockType() == FARMLAND;
    }

    @Override
    public void onRandomUpdate(BlockStateWithPos blockState) {
        if (blockState.pos().dimension().getInternalLightLevel(blockState.pos()) < 9) return;
        if (ThreadLocalRandom.current().nextInt(2) != 1) return;

        var oldGrowth = blockState.blockState().getPropertyValue(GROWTH);
        if (oldGrowth < GROWTH.getMax()) {
            grow(blockState.pos().dimension(), blockState.pos(), oldGrowth + 1);
        }
    }

    @Override
    public boolean onInteract(ItemStack itemStack, Dimension dimension, PlayerInteractInfo interactInfo) {
        if (super.onInteract(itemStack, dimension, interactInfo)) return true;

        if (itemStack instanceof ItemBoneMealStack) {
            var blockState = dimension.getBlockState(interactInfo.clickBlockPos());
            if (blockState.getPropertyValue(GROWTH) < GROWTH.getMax()) {
                int newAge = ThreadLocalRandom.current().nextInt(3) + 2; //Between 2 and 5
                grow(dimension, interactInfo.clickBlockPos(), newAge);
                //TODO: BoneMeal particle
                interactInfo.player().tryConsumeItemInHand();
            }
        }

        return true;
    }

    public void grow(Dimension dimension, Vector3ic pos, Integer newAge) {
        if (newAge < GROWTH.getMin()) newAge = GROWTH.getMin();
        if (newAge > GROWTH.getMax()) newAge = GROWTH.getMax();

        //TODO: event
        dimension.updateBlockProperty(GROWTH, newAge, pos);
    }*/
}
