package cn.allay.api.entity.interfaces;

import cn.allay.api.client.Client;
import cn.allay.api.component.annotation.ComponentIdentifier;
import cn.allay.api.component.annotation.Dependency;
import cn.allay.api.component.annotation.Impl;
import cn.allay.api.component.annotation.Manager;
import cn.allay.api.component.interfaces.ComponentManager;
import cn.allay.api.container.Container;
import cn.allay.api.container.FullContainerType;
import cn.allay.api.container.impl.*;
import cn.allay.api.data.VanillaEntityId;
import cn.allay.api.entity.Entity;
import cn.allay.api.entity.component.attribute.EntityAttributeComponent;
import cn.allay.api.entity.component.attribute.EntityAttributeComponentImpl;
import cn.allay.api.entity.component.base.EntityBaseComponentImpl;
import cn.allay.api.entity.component.base.EntityPlayerBaseComponent;
import cn.allay.api.entity.component.container.EntityContainerHolderComponent;
import cn.allay.api.entity.component.container.EntityContainerHolderComponentImpl;
import cn.allay.api.entity.component.container.EntityContainerViewerComponent;
import cn.allay.api.entity.init.EntityInitInfo;
import cn.allay.api.entity.init.EntityPlayerInitInfo;
import cn.allay.api.entity.type.EntityType;
import cn.allay.api.entity.type.EntityTypeBuilder;
import cn.allay.api.identifier.Identifier;
import cn.allay.api.utils.MathUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnmodifiableView;
import org.joml.primitives.AABBf;
import org.joml.primitives.AABBfc;

import java.util.Objects;
import java.util.function.Function;

import static cn.allay.api.entity.component.attribute.EntityAttributeComponentImpl.basicAttributes;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface EntityPlayer extends
        Entity,
        EntityPlayerBaseComponent,
        EntityAttributeComponent,
        EntityContainerHolderComponent,
        EntityContainerViewerComponent {
    EntityType<EntityPlayer> PLAYER_TYPE = EntityTypeBuilder
            .builder(EntityPlayer.class)
            .vanillaEntity(VanillaEntityId.PLAYER)
            .addComponent(
                    info -> new EntityPlayerBaseComponentImpl(info, e -> new AABBf(-0.3f, 0.0f, -0.3f, 0.3f, 1.8f, 0.3f)),
                    EntityPlayerBaseComponentImpl.class
            )
            .addComponent(info -> new EntityAttributeComponentImpl(basicAttributes()), EntityAttributeComponentImpl.class)
            .addComponent(info -> new EntityContainerHolderComponentImpl(
                            new PlayerInventoryContainer(((EntityPlayerInitInfo) info).getClient()),
                            new PlayerCursorContainer(),
                            new PlayerCreatedOutputContainer(),
                            new PlayerArmorContainer(),
                            new PlayerOffhandContainer()
                    ),
                    EntityContainerHolderComponentImpl.class)
            .addComponent(info -> new EntityPlayerContainerViewerComponentImpl(), EntityPlayerContainerViewerComponentImpl.class)
            .build();

    //<editor-fold desc="EntityPlayerBaseComponentImpl">
    class EntityPlayerBaseComponentImpl extends EntityBaseComponentImpl<EntityPlayer> implements EntityPlayerBaseComponent {

        @ComponentIdentifier
        public static final Identifier IDENTIFIER = EntityBaseComponentImpl.IDENTIFIER;

        @Dependency
        protected EntityContainerHolderComponent containerHolderComponent;
        protected Client client;
        protected boolean sprinting;
        protected boolean sneaking;
        protected boolean swimming;
        protected boolean gliding;
        protected boolean crawling;

        public EntityPlayerBaseComponentImpl(EntityInitInfo<EntityPlayer> info, Function<EntityPlayer, AABBfc> aabbGetter) {
            super(info, aabbGetter);
            if (info instanceof EntityPlayerInitInfo playerInitInfo) {
                client = playerInitInfo.getClient();
            } else {
                throw new IllegalArgumentException("EntityPlayerInitInfo is required for EntityPlayer");
            }
        }

        @Override
        @Impl
        public void tick() {
            super.tick();
            var world = location.world;
            //pick up items
            var pickUpArea = new AABBf(
                    location.x - 0.7125f,
                    location.y - 0.7125f,
                    location.z - 0.7125f,
                    location.x + 0.7125f,
                    location.y + 0.7125f,
                    location.z + 0.7125f
            );
            var entityItems = world.getEntityPhysicsService().computeCollidingEntities(pickUpArea)
                    .stream()
                    .filter(entity -> entity instanceof EntityItem entityItem && entityItem.canBePicked())
                    .toList();
            for (var entityItem : entityItems) {
                var item = ((EntityItem) entityItem).getItemStack();
                var inventory = Objects.requireNonNull(containerHolderComponent.getContainer(FullContainerType.PLAYER_INVENTORY));
                var remain = inventory.tryAddItem(item, true);
                if (remain == null) {
                    TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
                    takeItemEntityPacket.setRuntimeEntityId(uniqueId);
                    takeItemEntityPacket.setItemRuntimeEntityId(entityItem.getUniqueId());
                    Objects.requireNonNull(world.getChunkService().getChunkByLevelPos((int) location.x, (int) location.z)).sendChunkPacket(takeItemEntityPacket);
                    world.removeEntity(entityItem);
                }
            }
        }

        @Override
        @Impl
        public void spawnTo(Client client) {
            if (this.client != client)
                super.spawnTo(client);
        }

        @Override
        @Impl
        public void despawnFrom(Client client) {
            if (this.client != client)
                super.despawnFrom(client);
        }

        @Override
        @Impl
        public BedrockPacket createSpawnPacket() {
            var addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setRuntimeEntityId(uniqueId);
            addPlayerPacket.setUniqueEntityId(uniqueId);
            addPlayerPacket.setUuid(client.getLoginData().getUuid());
            addPlayerPacket.setUsername(client.getName());
            addPlayerPacket.setPlatformChatId(client.getLoginData().getDeviceInfo().getDeviceId());
            addPlayerPacket.setPosition(Vector3f.from(location.x(), location.y() + getBaseOffset(), location.z()));
            addPlayerPacket.setMotion(Vector3f.from(motion.x(), motion.y(), motion.z()));
            addPlayerPacket.setRotation(Vector3f.from(location.pitch(), location.yaw(), location.headYaw()));
            addPlayerPacket.setGameType(client.getGameType());
            addPlayerPacket.getMetadata().putAll(this.metadata.getEntityDataMap());
            addPlayerPacket.setDeviceId(client.getLoginData().getDeviceInfo().getDeviceId());
            addPlayerPacket.setHand(ItemData.AIR);//TODO: itemInHand
            return addPlayerPacket;
        }

        @Override
        @Impl
        public Client getClient() {
            return client;
        }

        @Override
        @Impl
        public void setSprinting(boolean sprinting) {
            this.sprinting = sprinting;
        }

        @Override
        @Impl
        public boolean isSprinting() {
            return sprinting;
        }

        @Override
        @Impl
        public void setSneaking(boolean sneaking) {
            this.sneaking = sneaking;
        }

        @Override
        @Impl
        public boolean isSneaking() {
            return sneaking;
        }

        @Override
        @Impl
        public void setSwimming(boolean swimming) {
            this.swimming = swimming;
        }

        @Override
        @Impl
        public boolean isSwimming() {
            return swimming;
        }

        @Override
        @Impl
        public void setGliding(boolean gliding) {
            this.gliding = gliding;
        }

        @Override
        @Impl
        public boolean isGliding() {
            return gliding;
        }

        @Override
        @Impl
        public void setCrawling(boolean crawling) {
            this.crawling = crawling;
        }

        @Override
        @Impl
        public boolean isCrawling() {
            return crawling;
        }

        @Override
        @Impl
        public int getHandSlot() {
            return containerHolderComponent.getContainer(FullContainerType.PLAYER_INVENTORY).getHandSlot();
        }

        @Override
        @Impl
        public void setHandSlot(@Range(from = 0, to = 8) int handSlot) {
            var inv = containerHolderComponent.getContainer(FullContainerType.PLAYER_INVENTORY);
            inv.setHandSlot(handSlot);
            var itemStack = inv.getItemStack(handSlot);

            var mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setRuntimeEntityId(uniqueId);
            mobEquipmentPacket.setItem(itemStack.toNetworkItemData());
            mobEquipmentPacket.setInventorySlot(handSlot);
            mobEquipmentPacket.setHotbarSlot(handSlot);

            sendPacketToViewers(mobEquipmentPacket);
        }

        @Override
        @Impl
        public NbtMap saveNBT() {
            return super.saveNBT().toBuilder()
                    .putList(
                            "Offhand",
                            NbtType.COMPOUND,
                            containerHolderComponent.getContainer(FullContainerType.OFFHAND).saveNBT())
                    .putList(
                            "Inventory",
                            NbtType.COMPOUND,
                            containerHolderComponent.getContainer(FullContainerType.PLAYER_INVENTORY).saveNBT())
                    .putList(
                            "Armor",
                            NbtType.COMPOUND,
                            containerHolderComponent.getContainer(FullContainerType.ARMOR).saveNBT())
                    .build();
        }

        @Override
        @Impl
        public void loadNBT(NbtMap nbt) {
            super.loadNBT(nbt);
            if (nbt.containsKey("Offhand")) {
                containerHolderComponent.getContainer(FullContainerType.OFFHAND).loadNBT(nbt.getList("Offhand", NbtType.COMPOUND));
            }
            if (nbt.containsKey("Inventory")) {
                containerHolderComponent.getContainer(FullContainerType.PLAYER_INVENTORY).loadNBT(nbt.getList("Inventory", NbtType.COMPOUND));
            }
            if (nbt.containsKey("Armor")) {
                containerHolderComponent.getContainer(FullContainerType.ARMOR).loadNBT(nbt.getList("Armor", NbtType.COMPOUND));
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="EntityPlayerContainerViewerComponentImpl">
    class EntityPlayerContainerViewerComponentImpl implements EntityContainerViewerComponent {

        @ComponentIdentifier
        protected static final Identifier IDENTIFIER = new Identifier("minecraft:entity_inventory_viewer_component");

        protected byte idCounter = 0;
        @Manager
        protected ComponentManager<EntityPlayer> manager;
        @Dependency
        protected EntityContainerHolderComponent containerHolderComponent;
        protected Client client;

        protected HashBiMap<Byte, Container> id2ContainerBiMap = HashBiMap.create(new Byte2ObjectOpenHashMap<>());
        protected HashBiMap<FullContainerType<?>, Container> type2ContainerBiMap = HashBiMap.create(new Object2ObjectOpenHashMap<>());

        @Override
        public void onInitFinish() {
            client = manager.getComponentedObject().getClient();
        }

        @Override
        @Impl
        public byte assignInventoryId() {
            if (idCounter + 1 >= 100) {
                idCounter = 0;
            }
            return idCounter++;
        }

        @Override
        @Impl
        public void sendContents(Container container) {
            var id = id2ContainerBiMap.inverse().get(container);
            if (id == null)
                throw new IllegalArgumentException("This viewer did not open the container " + container.getContainerType());
            sendContentsWithSpecificContainerId(container, id);
        }

        @Override
        @Impl
        public void sendContentsWithSpecificContainerId(Container container, int containerId) {
            var inventoryContentPacket = new InventoryContentPacket();
            inventoryContentPacket.setContainerId(containerId);
            inventoryContentPacket.setContents(container.toNetworkItemData());
            client.sendPacket(inventoryContentPacket);
        }

        @Override
        @Impl
        public void sendContentsWithSpecificContainerId(Container container, int containerId, int slot) {
            var inventorySlotPacket = new InventorySlotPacket();
            inventorySlotPacket.setContainerId(containerId);
            inventorySlotPacket.setSlot(slot);
            inventorySlotPacket.setItem(container.getItemStack(slot).toNetworkItemData());
            client.sendPacket(inventorySlotPacket);
        }

        @Override
        @Impl
        public void sendContent(Container container, int slot) {
            var id = id2ContainerBiMap.inverse().get(container);
            if (id == null)
                throw new IllegalArgumentException("This viewer did not open the container " + container.getContainerType());
            sendContentsWithSpecificContainerId(container, id, slot);
        }

        @Override
        @Impl
        public void onOpen(byte assignedId, Container container) {
            var containerOpenPacket = new ContainerOpenPacket();
            containerOpenPacket.setId(assignedId);
            var containerType = container.getContainerType();
            containerOpenPacket.setType(containerType.toNetworkType());
            if (container.hasBlockPos()) {
                containerOpenPacket.setBlockPosition(MathUtils.JOMLVecTocbVec(container.getBlockPos()));
            } else {
                var location = manager.getComponentedObject().getLocation();
                containerOpenPacket.setBlockPosition(Vector3i.from(location.x(), location.y(), location.z()));
            }
            client.sendPacket(containerOpenPacket);

            id2ContainerBiMap.put(assignedId, container);
            type2ContainerBiMap.put(container.getContainerType(), container);

            //We should send the container's contents to client if the container is not held by the entity
            if (containerHolderComponent.getContainer(containerType) == null) {
                sendContents(container);
            }
        }

        @Override
        @Impl
        public void onClose(byte assignedId, Container container) {
            if (!id2ContainerBiMap.containsKey(assignedId))
                throw new IllegalArgumentException("Trying to close a container which is not opened! Type: " + container.getContainerType());
            var containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setId(assignedId);
            client.sendPacket(containerClosePacket);

            type2ContainerBiMap.remove(id2ContainerBiMap.remove(assignedId).getContainerType());
        }

        @Override
        @Impl
        public void onSlotChange(Container container, int slot) {
            var id = id2ContainerBiMap.inverse().get(container);
            //"0" is player's inventory
            sendContentsWithSpecificContainerId(container, id != null ? id : 0, slot);
        }

        @Override
        @Nullable
        @Impl
        public <T extends Container> T getOpenedContainer(FullContainerType<T> type) {
            return (T) type2ContainerBiMap.get(type);
        }

        @Override
        @Nullable
        @Impl
        public Container getOpenedContainer(byte id) {
            return id2ContainerBiMap.get(id);
        }

        @Override
        @Impl
        public @UnmodifiableView BiMap<Byte, Container> getId2ContainerBiMap() {
            return id2ContainerBiMap;
        }

        @Override
        @Impl
        public @UnmodifiableView BiMap<FullContainerType<?>, Container> getType2ContainerBiMap() {
            return type2ContainerBiMap;
        }
    }
    //</editor-fold>
}

