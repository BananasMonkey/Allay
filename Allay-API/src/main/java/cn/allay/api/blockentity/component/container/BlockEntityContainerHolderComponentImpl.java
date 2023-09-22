package cn.allay.api.blockentity.component.container;

import cn.allay.api.block.component.event.BlockOnInteractEvent;
import cn.allay.api.block.component.event.BlockOnReplaceEvent;
import cn.allay.api.component.annotation.ComponentEventListener;
import cn.allay.api.component.annotation.ComponentIdentifier;
import cn.allay.api.component.annotation.Impl;
import cn.allay.api.container.Container;
import cn.allay.api.container.ContainerHolder;
import cn.allay.api.entity.init.SimpleEntityInitInfo;
import cn.allay.api.entity.interfaces.EntityItem;
import cn.allay.api.identifier.Identifier;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Allay Project 2023/9/16
 *
 * @author daoge_cmd
 */
public class BlockEntityContainerHolderComponentImpl implements BlockEntityContainerHolderComponent, ContainerHolder {
    @ComponentIdentifier
    protected static final Identifier IDENTIFIER = new Identifier("minecraft:block_entity_inventory_holder_component");
    protected Container container;

    public BlockEntityContainerHolderComponentImpl(Container container) {
        this.container = container;
    }

    @Override
    @Impl
    public <T extends Container> T getContainer() {
        return (T) container;
    }

    @Override
    @Impl
    public void setContainer(Container container) {
        this.container = container;
    }

    @ComponentEventListener
    private void onInteract(BlockOnInteractEvent event) {
        var player = event.player();
        var blockPos = event.blockPos();
        var world = event.world();
        if (player == null || player.isSneaking()) return;
        Objects.requireNonNull(container).addViewer(player);
    }

    @ComponentEventListener
    private void onReplace(BlockOnReplaceEvent event) {
        var pos = event.currentBlockState().pos();
        var world = pos.world();
        var rand = ThreadLocalRandom.current();
        for (var itemStack : container.getItemStacks()) {
            if (itemStack != Container.AIR_STACK) {
                var entity = EntityItem.ITEM_TYPE.createEntity(
                        SimpleEntityInitInfo
                                .builder()
                                .pos(pos.x() + rand.nextFloat(0.5f) + 0.25f, pos.y() + rand.nextFloat(0.5f) + 0.25f, pos.z() + rand.nextFloat(0.5f) + 0.25f)
                                .world(world)
                                .motion(rand.nextFloat(0.2f) - 0.1f, 0.2f, rand.nextFloat(0.2f) - 0.1f)
                                .build()
                );
                entity.setItemStack(itemStack);
                entity.setPickupDelay(10);
                world.addEntity(entity);
            }
        }
    }
}
