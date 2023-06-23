package cn.allay.server.block.attribute;

import cn.allay.api.block.component.impl.attribute.BlockAttributes;
import cn.allay.api.block.component.impl.attribute.VanillaBlockAttributeRegistry;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.registry.RegistryLoader;
import cn.allay.api.registry.SimpleMappedRegistry;
import cn.allay.api.utils.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author daoge_cmd <br>
 * @date 2023/3/26 <br>
 * Allay Project <br>
 */
@Slf4j
public final class AllayVanillaBlockAttributeRegistry extends SimpleMappedRegistry<VanillaBlockId, Map<Integer, BlockAttributes>, Map<VanillaBlockId, Map<Integer, BlockAttributes>>> implements VanillaBlockAttributeRegistry {
    public AllayVanillaBlockAttributeRegistry(RegistryLoader<Void, Map<VanillaBlockId, Map<Integer, BlockAttributes>>> loader) {
        super(null, loader);
    }

    public static class Loader implements RegistryLoader<Void, Map<VanillaBlockId, Map<Integer, BlockAttributes>>> {

        @SneakyThrows
        @Override
        public Map<VanillaBlockId, Map<Integer, BlockAttributes>> load(Void input) {
            log.info("Start loading vanilla block attribute data registry...");
            try (var reader = getReader()) {
                var element = JsonParser.parseReader(reader);
                var loaded = new HashMap<VanillaBlockId, Map<Integer, BlockAttributes>>();
                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    VanillaBlockId type;
                    try {
                        type = VanillaBlockId.valueOf(StringUtils.fastTwoPartSplit(jsonElement.getAsJsonObject().get("name").getAsString(), ":", "")[1].toUpperCase());
                    } catch (IllegalArgumentException ignore) {
                        log.error("Unknown block name: " + jsonElement.getAsJsonObject().get("name"));
                        continue;
                    }
                    var blockAttributes = BlockAttributes.of(jsonElement.toString());
                    if (!loaded.containsKey(type))
                        loaded.put(type, new HashMap<>());
                    //TODO: check
                    if (type != VanillaBlockId.UNKNOWN)
                        loaded.get(type).put(Integer.parseUnsignedInt(jsonElement.getAsJsonObject().get("blockStateHash").getAsString()), blockAttributes);
                    else //Special case
                        loaded.get(type).put(jsonElement.getAsJsonObject().get("blockStateHash").getAsInt(), blockAttributes);
                }
                log.info("Loaded vanilla block attribute data registry successfully");
                return loaded;
            }
        }

        protected Reader getReader() {
            return new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(
                                    AllayVanillaBlockAttributeRegistry.class
                                            .getClassLoader()
                                            .getResourceAsStream("block_attributes.json"),
                                    "block_attributes.json is missing!")
                    )
            );
        }
    }
}
