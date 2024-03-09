package org.allaymc.api.data;

import lombok.Getter;
import org.allaymc.api.client.data.Identifier;
import org.allaymc.api.world.biome.BiomeType;

import java.util.HashMap;

/**
 * Automatically generated by {@code org.allaymc.codegen.VanillaBiomeIdEnumGen} <br>
 * Allay Project <p>
 * @author daoge_cmd | Cool_Loong
 */
@Getter
public enum VanillaBiomeId implements BiomeType {
  OCEAN(new Identifier("minecraft:ocean"), 0, "Ocean"),

  PLAINS(new Identifier("minecraft:plains"), 1, "Plain"),

  DESERT(new Identifier("minecraft:desert"), 2, "Desert"),

  EXTREME_HILLS(new Identifier("minecraft:extreme_hills"), 3, "ExtremeHills"),

  FOREST(new Identifier("minecraft:forest"), 4, "Forest"),

  TAIGA(new Identifier("minecraft:taiga"), 5, "Taiga"),

  SWAMPLAND(new Identifier("minecraft:swampland"), 6, "Swamp"),

  RIVER(new Identifier("minecraft:river"), 7, "River"),

  HELL(new Identifier("minecraft:hell"), 8, "Hell"),

  THE_END(new Identifier("minecraft:the_end"), 9, "TheEnd"),

  LEGACY_FROZEN_OCEAN(new Identifier("minecraft:legacy_frozen_ocean"), 10, "Ocean"),

  FROZEN_RIVER(new Identifier("minecraft:frozen_river"), 11, "River"),

  ICE_PLAINS(new Identifier("minecraft:ice_plains"), 12, "Ice"),

  ICE_MOUNTAINS(new Identifier("minecraft:ice_mountains"), 13, "Ice"),

  MUSHROOM_ISLAND(new Identifier("minecraft:mushroom_island"), 14, "MushroomIsland"),

  MUSHROOM_ISLAND_SHORE(new Identifier("minecraft:mushroom_island_shore"), 15, "MushroomIsland"),

  BEACH(new Identifier("minecraft:beach"), 16, "Beach"),

  DESERT_HILLS(new Identifier("minecraft:desert_hills"), 17, "Desert"),

  FOREST_HILLS(new Identifier("minecraft:forest_hills"), 18, "Forest"),

  TAIGA_HILLS(new Identifier("minecraft:taiga_hills"), 19, "Taiga"),

  EXTREME_HILLS_EDGE(new Identifier("minecraft:extreme_hills_edge"), 20, "ExtremeHills"),

  JUNGLE(new Identifier("minecraft:jungle"), 21, "Jungle"),

  JUNGLE_HILLS(new Identifier("minecraft:jungle_hills"), 22, "Jungle"),

  JUNGLE_EDGE(new Identifier("minecraft:jungle_edge"), 23, "Jungle"),

  DEEP_OCEAN(new Identifier("minecraft:deep_ocean"), 24, "Ocean"),

  STONE_BEACH(new Identifier("minecraft:stone_beach"), 25, "StoneBeach"),

  COLD_BEACH(new Identifier("minecraft:cold_beach"), 26, "Beach"),

  BIRCH_FOREST(new Identifier("minecraft:birch_forest"), 27, "Forest"),

  BIRCH_FOREST_HILLS(new Identifier("minecraft:birch_forest_hills"), 28, "Forest"),

  ROOFED_FOREST(new Identifier("minecraft:roofed_forest"), 29, "Forest"),

  COLD_TAIGA(new Identifier("minecraft:cold_taiga"), 30, "Taiga"),

  COLD_TAIGA_HILLS(new Identifier("minecraft:cold_taiga_hills"), 31, "Taiga"),

  MEGA_TAIGA(new Identifier("minecraft:mega_taiga"), 32, "Taiga"),

  MEGA_TAIGA_HILLS(new Identifier("minecraft:mega_taiga_hills"), 33, "Taiga"),

  EXTREME_HILLS_PLUS_TREES(new Identifier("minecraft:extreme_hills_plus_trees"), 34, "ExtremeHills"),

  SAVANNA(new Identifier("minecraft:savanna"), 35, "Savanna"),

  SAVANNA_PLATEAU(new Identifier("minecraft:savanna_plateau"), 36, "Savanna"),

  MESA(new Identifier("minecraft:mesa"), 37, "Mesa"),

  MESA_PLATEAU_STONE(new Identifier("minecraft:mesa_plateau_stone"), 38, "Mesa"),

  MESA_PLATEAU(new Identifier("minecraft:mesa_plateau"), 39, "Mesa"),

  WARM_OCEAN(new Identifier("minecraft:warm_ocean"), 40, "Ocean"),

  DEEP_WARM_OCEAN(new Identifier("minecraft:deep_warm_ocean"), 41, "Ocean"),

  LUKEWARM_OCEAN(new Identifier("minecraft:lukewarm_ocean"), 42, "Ocean"),

  DEEP_LUKEWARM_OCEAN(new Identifier("minecraft:deep_lukewarm_ocean"), 43, "Ocean"),

  COLD_OCEAN(new Identifier("minecraft:cold_ocean"), 44, "Ocean"),

  DEEP_COLD_OCEAN(new Identifier("minecraft:deep_cold_ocean"), 45, "Ocean"),

  FROZEN_OCEAN(new Identifier("minecraft:frozen_ocean"), 46, "Ocean"),

  DEEP_FROZEN_OCEAN(new Identifier("minecraft:deep_frozen_ocean"), 47, "Ocean"),

  BAMBOO_JUNGLE(new Identifier("minecraft:bamboo_jungle"), 48, "Jungle"),

  BAMBOO_JUNGLE_HILLS(new Identifier("minecraft:bamboo_jungle_hills"), 49, "Jungle"),

  SUNFLOWER_PLAINS(new Identifier("minecraft:sunflower_plains"), 129, "Plain"),

  DESERT_MUTATED(new Identifier("minecraft:desert_mutated"), 130, "Desert"),

  EXTREME_HILLS_MUTATED(new Identifier("minecraft:extreme_hills_mutated"), 131, "ExtremeHills"),

  FLOWER_FOREST(new Identifier("minecraft:flower_forest"), 132, "Forest"),

  TAIGA_MUTATED(new Identifier("minecraft:taiga_mutated"), 133, "Taiga"),

  SWAMPLAND_MUTATED(new Identifier("minecraft:swampland_mutated"), 134, "Swamp"),

  ICE_PLAINS_SPIKES(new Identifier("minecraft:ice_plains_spikes"), 140, "Ice"),

  JUNGLE_MUTATED(new Identifier("minecraft:jungle_mutated"), 149, "Jungle"),

  JUNGLE_EDGE_MUTATED(new Identifier("minecraft:jungle_edge_mutated"), 151, "Jungle"),

  BIRCH_FOREST_MUTATED(new Identifier("minecraft:birch_forest_mutated"), 155, "Forest"),

  BIRCH_FOREST_HILLS_MUTATED(new Identifier("minecraft:birch_forest_hills_mutated"), 156, "Forest"),

  ROOFED_FOREST_MUTATED(new Identifier("minecraft:roofed_forest_mutated"), 157, "Forest"),

  COLD_TAIGA_MUTATED(new Identifier("minecraft:cold_taiga_mutated"), 158, "Taiga"),

  REDWOOD_TAIGA_MUTATED(new Identifier("minecraft:redwood_taiga_mutated"), 160, "Taiga"),

  REDWOOD_TAIGA_HILLS_MUTATED(new Identifier("minecraft:redwood_taiga_hills_mutated"), 161, "Taiga"),

  EXTREME_HILLS_PLUS_TREES_MUTATED(new Identifier("minecraft:extreme_hills_plus_trees_mutated"), 162, "ExtremeHills"),

  SAVANNA_MUTATED(new Identifier("minecraft:savanna_mutated"), 163, "Savanna"),

  SAVANNA_PLATEAU_MUTATED(new Identifier("minecraft:savanna_plateau_mutated"), 164, "Savanna"),

  MESA_BRYCE(new Identifier("minecraft:mesa_bryce"), 165, "Mesa"),

  MESA_PLATEAU_STONE_MUTATED(new Identifier("minecraft:mesa_plateau_stone_mutated"), 166, "Mesa"),

  MESA_PLATEAU_MUTATED(new Identifier("minecraft:mesa_plateau_mutated"), 167, "Mesa"),

  SOULSAND_VALLEY(new Identifier("minecraft:soulsand_valley"), 178, "DataDriven"),

  CRIMSON_FOREST(new Identifier("minecraft:crimson_forest"), 179, "DataDriven"),

  WARPED_FOREST(new Identifier("minecraft:warped_forest"), 180, "DataDriven"),

  BASALT_DELTAS(new Identifier("minecraft:basalt_deltas"), 181, "DataDriven"),

  JAGGED_PEAKS(new Identifier("minecraft:jagged_peaks"), 182, "DataDriven"),

  FROZEN_PEAKS(new Identifier("minecraft:frozen_peaks"), 183, "DataDriven"),

  SNOWY_SLOPES(new Identifier("minecraft:snowy_slopes"), 184, "DataDriven"),

  GROVE(new Identifier("minecraft:grove"), 185, "DataDriven"),

  MEADOW(new Identifier("minecraft:meadow"), 186, "DataDriven"),

  LUSH_CAVES(new Identifier("minecraft:lush_caves"), 187, "DataDriven"),

  DRIPSTONE_CAVES(new Identifier("minecraft:dripstone_caves"), 188, "DataDriven"),

  STONY_PEAKS(new Identifier("minecraft:stony_peaks"), 189, "DataDriven"),

  DEEP_DARK(new Identifier("minecraft:deep_dark"), 190, "DataDriven"),

  MANGROVE_SWAMP(new Identifier("minecraft:mangrove_swamp"), 191, "DataDriven"),

  CHERRY_GROVE(new Identifier("minecraft:cherry_grove"), 192, "DataDriven");

  public static final VanillaBiomeId[] VALUES = values();

  private static final VanillaBiomeId[] MAP1;

  private static final HashMap<Identifier, VanillaBiomeId> MAP2;

  static {
    MAP1 = new VanillaBiomeId[256];MAP2 = new HashMap<>();
    for (var b : values()) {
        MAP1[b.id] = b;
        MAP2.put(b.identifier, b);
    }}

  private final Identifier identifier;

  private final int id;

  private final String type;

  VanillaBiomeId(Identifier identifier, int id, String type) {
    this.identifier = identifier;
    this.id = id;
    this.type = type;
  }

  public static BiomeType fromId(int id) {
    return MAP1[id];
  }

  public static BiomeType fromIdentifier(Identifier identifier) {
    return MAP2.get(identifier);
  }
}
