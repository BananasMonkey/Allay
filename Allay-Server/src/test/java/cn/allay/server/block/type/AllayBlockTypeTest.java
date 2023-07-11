package cn.allay.server.block.type;

import cn.allay.api.block.component.impl.attribute.BlockAttributeComponentImpl;
import cn.allay.api.block.component.impl.attribute.BlockAttributes;
import cn.allay.api.block.property.enums.*;
import cn.allay.api.block.property.type.BlockPropertyType;
import cn.allay.api.block.property.type.BooleanPropertyType;
import cn.allay.api.block.property.type.EnumPropertyType;
import cn.allay.api.block.property.type.IntPropertyType;
import cn.allay.api.block.type.BlockInitInfo;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.component.interfaces.ComponentProvider;
import cn.allay.api.data.VanillaBlockPropertyTypes;
import cn.allay.api.data.VanillaBlockTypes;
import cn.allay.api.math.vector.Pos3i;
import cn.allay.server.block.component.TestComponentImpl;
import cn.allay.server.block.component.TestComponentImplV2;
import cn.allay.server.block.component.TestCustomBlockComponentImpl;
import cn.allay.testutils.AllayTestExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static cn.allay.api.component.interfaces.ComponentProvider.of;
import static cn.allay.api.component.interfaces.ComponentProvider.ofSingleton;
import static cn.allay.server.block.type.AllayBlockType.computeSpecialValue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Allay Project 2023/4/16
 *
 * @author daoge_cmd
 */
@ExtendWith(AllayTestExtension.class)
class AllayBlockTypeTest {
    static BlockType<TestBlock> testBlockType1;
    static BlockType<TestBlock> testBlockType2;
    static BlockPropertyType<Boolean> TEST_BOOLEAN_PROPERTY_TYPE;
    static BlockPropertyType<Integer> TEST_INT_PROPERTY_TYPE;
    static BlockPropertyType<TestEnum> TEST_ENUM_PROPERTY_TYPE;

    @BeforeAll
    static void init() {
        TEST_BOOLEAN_PROPERTY_TYPE = BooleanPropertyType.of("test_boolean", false);
        TEST_INT_PROPERTY_TYPE = IntPropertyType.of("test_int", 0, 10, 0);
        TEST_ENUM_PROPERTY_TYPE = EnumPropertyType.of("test_enum", TestEnum.class, TestEnum.A);
        testBlockType1 = AllayBlockType
                .builder(TestBlock.class)
                .identifier("minecraft:test_block")
                .withProperties(
                        TEST_BOOLEAN_PROPERTY_TYPE,
                        TEST_INT_PROPERTY_TYPE,
                        TEST_ENUM_PROPERTY_TYPE)
                .setComponents(List.of(
                        ComponentProvider.of(TestComponentImpl::new, TestComponentImpl.class),
                        ComponentProvider.ofSingleton(BlockAttributeComponentImpl.ofGlobalStatic(BlockAttributes.builder().burnChance(2).build()))
                ))
                .addCustomBlockComponent(new TestCustomBlockComponentImpl())
                .addBasicComponents()
                .build();
        testBlockType2 = AllayBlockType
                .builder(TestBlock.class)
                .identifier("minecraft:test_block2")
                .withProperties(
                        TEST_BOOLEAN_PROPERTY_TYPE,
                        TEST_INT_PROPERTY_TYPE,
                        TEST_ENUM_PROPERTY_TYPE)
                .setComponents(List.of(
                        ComponentProvider.of(TestComponentImpl::new, TestComponentImpl.class),
                        ComponentProvider.ofSingleton(BlockAttributeComponentImpl.ofDynamic(blockState -> BlockAttributes.builder().burnChance(3).build()))
                ))
                .addCustomBlockComponent(new TestCustomBlockComponentImpl())
                .addBasicComponents()
                .build();
        assertThrows(BlockTypeBuildException.class,
                () -> {
                    AllayBlockType
                            .builder(TestBlock.class)
                            .identifier("minecraft:test_block_v2")
                            .withProperties(
                                    TEST_BOOLEAN_PROPERTY_TYPE,
                                    TEST_INT_PROPERTY_TYPE,
                                    TEST_ENUM_PROPERTY_TYPE)
                            .setComponents(List.of(
                                    of(TestComponentImplV2::new, TestComponentImplV2.class),
                                    ofSingleton(BlockAttributeComponentImpl.ofGlobalStatic(BlockAttributes.DEFAULT))
                            ))
                            .addCustomBlockComponent(new TestCustomBlockComponentImpl())
                            .addBasicComponents()
                            .build();
                }
        );
    }

    @Test
    void testBlockType() {
        assertNotNull(testBlockType1);
    }

    @Test
    void testCommon() {
        var block = testBlockType1.createBlock(new BlockInitInfo.Simple(Pos3i.of(1, 2, 3, null)));
        //Auto-registered component
        assertTrue(block.getTestFlag());
        assertEquals(1, block.getPosition().x());
        assertEquals(2, block.getPosition().y());
        assertEquals(3, block.getPosition().z());
        assertEquals(testBlockType1, block.getBlockType());
        //Test block properties
        assertFalse(block.getProperty(TEST_BOOLEAN_PROPERTY_TYPE));
        block.setProperty(TEST_BOOLEAN_PROPERTY_TYPE, true);
        assertTrue(block.getProperty(TEST_BOOLEAN_PROPERTY_TYPE));
        assertEquals(0, block.getProperty(TEST_INT_PROPERTY_TYPE));
        block.setProperty(TEST_INT_PROPERTY_TYPE, 5);
        assertEquals(5, block.getProperty(TEST_INT_PROPERTY_TYPE));
        assertEquals(TestEnum.A, block.getProperty(TEST_ENUM_PROPERTY_TYPE));
        block.setProperty(TEST_ENUM_PROPERTY_TYPE, TestEnum.B);
        assertEquals(TestEnum.B, block.getProperty(TEST_ENUM_PROPERTY_TYPE));
    }

    @Test
    void testRequirePropertyAnnotation() {
        assertThrows(
                BlockTypeBuildException.class,
                () -> AllayBlockType
                        .builder(TestBlock.class)
                        .identifier("minecraft:test_block")
                        .withProperties(
                                TEST_BOOLEAN_PROPERTY_TYPE,
//                                TEST_INT_PROPERTY_TYPE,
                                TEST_ENUM_PROPERTY_TYPE)
                        .setComponents(List.of(
                                of(TestComponentImpl::new, TestComponentImpl.class),
                                ofSingleton(BlockAttributeComponentImpl.ofGlobalStatic(BlockAttributes.DEFAULT))
                        ))
                        .addBasicComponents()
                        .build()
        );
    }

    @Test
    void testBlockStateHash() {
        var b1 = VanillaBlockTypes.COBBLED_DEEPSLATE_WALL_TYPE.createBlock(new BlockInitInfo.Simple(Pos3i.of(1, 2, 3, null)));
        b1.setProperty(VanillaBlockPropertyTypes.WALL_CONNECTION_TYPE_EAST, WallConnectionTypeEast.NONE);
        b1.setProperty(VanillaBlockPropertyTypes.WALL_CONNECTION_TYPE_NORTH, WallConnectionTypeNorth.TALL);
        b1.setProperty(VanillaBlockPropertyTypes.WALL_CONNECTION_TYPE_SOUTH, WallConnectionTypeSouth.SHORT);
        b1.setProperty(VanillaBlockPropertyTypes.WALL_CONNECTION_TYPE_WEST, WallConnectionTypeWest.NONE);
        b1.setProperty(VanillaBlockPropertyTypes.WALL_POST_BIT, true);
        assertEquals(1789459903, b1.getCurrentState().unsignedBlockStateHash());

        var b2 = VanillaBlockTypes.BLUE_CANDLE_TYPE.createBlock(new BlockInitInfo.Simple(Pos3i.of(1, 2, 3, null)));
        b2.setProperty(VanillaBlockPropertyTypes.CANDLES, 2);
        b2.setProperty(VanillaBlockPropertyTypes.LIT, false);
        assertEquals(4220034033L, b2.getCurrentState().unsignedBlockStateHash());

        var b3 = VanillaBlockTypes.CORAL_FAN_TYPE.createBlock(new BlockInitInfo.Simple(Pos3i.of(0, 1, 2, null)));
        b3.setProperty(VanillaBlockPropertyTypes.CORAL_COLOR, CoralColor.BLUE);
        b3.setProperty(VanillaBlockPropertyTypes.CORAL_FAN_DIRECTION, 0);
        assertEquals(781710940, b3.getCurrentState().unsignedBlockStateHash());
    }

    @Test
    void testBlockAttributes() {
        assertEquals(2, testBlockType1.createBlock(new BlockInitInfo.Simple(Pos3i.of(0, 1, 2, null))).getBlockAttributes().burnChance());
        assertEquals(3, testBlockType2.createBlock(new BlockInitInfo.Simple(Pos3i.of(0, 1, 2, null))).getBlockAttributes().burnChance());
    }

    @Test
    void testSpecialValue() {
        var values = new BlockPropertyType.BlockPropertyValue<?, ?, ?>[3];
        values[0] = (TEST_BOOLEAN_PROPERTY_TYPE.createValue(true));//1 bit
        values[1] = (TEST_INT_PROPERTY_TYPE.createValue(5));//4 bit
        values[2] = (TEST_ENUM_PROPERTY_TYPE.createValue(TestEnum.B));//2 bit
        int offset = 0;
        for (var value : values) offset += value.getPropertyType().getBitSize();
        //                      1 0101 01
        assertEquals(0b1010101, computeSpecialValue(offset, values));
        //                      1 0101 00
        values[2] = TEST_ENUM_PROPERTY_TYPE.createValue(TestEnum.A);
        assertEquals(0b1010100, computeSpecialValue(offset, values));
        //                      1 0000 00
        values[1] = TEST_INT_PROPERTY_TYPE.createValue(0);
        assertEquals(0b1000000, computeSpecialValue(offset, values));
    }
}