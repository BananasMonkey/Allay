package cn.allay.api.block.component.impl.base;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.data.BlockStateWithPos;
import cn.allay.api.block.blockupdate.*;
import cn.allay.api.block.component.BlockComponentImpl;
import cn.allay.api.block.data.BlockFace;
import cn.allay.api.block.type.BlockState;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.component.annotation.ComponentIdentifier;
import cn.allay.api.component.annotation.Impl;
import cn.allay.api.identifier.Identifier;
import lombok.Builder;
import org.joml.Vector3i;

/**
 * Allay Project 2023/4/8
 *
 * @author daoge_cmd
 */
public class BlockBaseComponentImpl implements BlockBaseComponent, BlockComponentImpl {

    @ComponentIdentifier
    public static final Identifier IDENTIFIER = new Identifier("minecraft:block_base_component");
    protected BlockType<? extends BlockBehavior> blockType;
    protected OnNeighborChanged onNeighborChanged = (blockState, neighborBlockState, blockFace) -> {};
    protected OnPlace onPlace = (currentBlockState, newBlockState) -> {};
    protected OnRandomUpdate onRandomUpdate = blockState -> {};
    protected OnReplace onReplace = (currentBlockState, newBlockState) -> {};
    protected OnScheduledUpdate onScheduledUpdate = blockState -> {};

    public BlockBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        this.blockType = blockType;
    }

    @Builder
    public BlockBaseComponentImpl(
            BlockType<? extends BlockBehavior> blockType,
            OnNeighborChanged onNeighborChanged,
            OnPlace onPlace,
            OnRandomUpdate onRandomUpdate,
            OnReplace onReplace,
            OnScheduledUpdate onScheduledUpdate) {
        this.blockType = blockType;
        this.onNeighborChanged = onNeighborChanged;
        this.onPlace = onPlace;
        this.onRandomUpdate = onRandomUpdate;
        this.onReplace = onReplace;
        this.onScheduledUpdate = onScheduledUpdate;
    }

    @Override
    @Impl
    public BlockType<? extends BlockBehavior> getBlockType() {
        return blockType;
    }

    @Override
    @Impl
    public void onNeighborChanged(BlockStateWithPos blockState, BlockStateWithPos neighborBlockState, BlockFace blockFace) {
        onNeighborChanged.onNeighborChanged(blockState, neighborBlockState, blockFace);
    }

    @Override
    @Impl
    public void onRandomUpdate(BlockStateWithPos blockState) {
        onRandomUpdate.onRandomUpdate(blockState);
    }

    @Override
    @Impl
    public void onScheduledUpdate(BlockStateWithPos blockState) {
        onScheduledUpdate.onScheduledUpdate(blockState);
    }

    @Override
    @Impl
    public void onReplace(BlockStateWithPos currentBlockState, BlockState newBlockState) {
        onReplace.onReplace(currentBlockState, newBlockState);
    }

    @Override
    @Impl
    public void onPlace(BlockStateWithPos currentBlockState, BlockState newBlockState) {
        onPlace.onPlace(currentBlockState, newBlockState);
    }
}
