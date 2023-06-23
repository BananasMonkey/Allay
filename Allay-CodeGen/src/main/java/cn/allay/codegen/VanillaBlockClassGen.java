package cn.allay.codegen;

import cn.allay.dependence.VanillaBlockId;
import com.squareup.javapoet.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.allay.codegen.CodeGen.BLOCK_PROPERTY_TYPE_INFO_FILE;

/**
 * Depend on VanillaBlockIdEnumGen execution
 * <p>
 * @author daoge_cmd | Cool_Loong<br>
 * @date 2023/4/8 <br>
 * Allay Project <br>
 */
public class VanillaBlockClassGen {

    public static final ClassName BLOCK_CLASS_NAME = ClassName.get("cn.allay.api.block", "Block");
    public static final ClassName VANILLA_BLOCK_ID_CLASS_NAME = ClassName.get("cn.allay.api.data", "VanillaBlockId");
    public static final ClassName VANILLA_BLOCK_PROPERTY_TYPES_CLASS_NAME = ClassName.get("cn.allay.api.data", "VanillaBlockPropertyTypes");
    public static final ClassName BLOCK_TYPE_CLASS_NAME = ClassName.get("cn.allay.api.block.type", "BlockType");
    public static final ClassName BLOCK_TYPE_BUILDER_CLASS_NAME = ClassName.get("cn.allay.api.block.type", "BlockTypeBuilder");
    public static Path FILE_OUTPUT_PATH_BASE = Path.of("Allay-API/src/main/java/cn/allay/api/block/impl");

    private static final TypeSpec.Builder TYPES_CLASS = TypeSpec.classBuilder("VanillaBlockTypes")
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc(
                    "@author: daoge_cmd <br>\n" +
                    "Allay Project <br>\n");

    @SneakyThrows
    public static void generate() {
        if (!Files.exists(FILE_OUTPUT_PATH_BASE)) Files.createDirectories(FILE_OUTPUT_PATH_BASE);
        for (var block : VanillaBlockId.values()) {
            var className = "Block" + Utils.convertToPascalCase(block.getIdentifier().path());
            var path = FILE_OUTPUT_PATH_BASE.resolve(className + ".java");
            if (!Files.exists(path)) {
                System.out.println("Generating " + className + ".java ...");
                generateBlockClass(className, path);
            }
            generateBlockType(block, className);
            var typesJavaFile = JavaFile
                    .builder("cn.allay.api.block.type", TYPES_CLASS.build())
                    .build();
            Files.writeString(Path.of("Allay-API/src/main/java/cn/allay/api/block/type/VanillaBlockTypes.java"), typesJavaFile.toString());
        }
    }

    private static void generateBlockClass(String className, Path path) throws IOException {
        TypeSpec.Builder codeBuilder = TypeSpec.interfaceBuilder(className)
                .addSuperinterface(BLOCK_CLASS_NAME)
                .addJavadoc(
                        "@author: daoge_cmd | Cool_Loong <br>\n" +
                                "Allay Project <br>\n")
                .addModifiers(Modifier.PUBLIC);
        var javaFile = JavaFile.builder("cn.allay.api.block.impl", codeBuilder.build()).build();
        System.out.println("Generating " + className + ".java ...");
        Files.writeString(path, javaFile.toString());
    }

    private static void generateBlockType(VanillaBlockId vanillaBlockId, String classNameStr) {
        var className = ClassName.get("cn.allay.api.block.impl", classNameStr);
        var initializer = CodeBlock.builder();
        initializer
                .add("$T\n        .builder($T.class)\n", BLOCK_TYPE_BUILDER_CLASS_NAME, className)
                .add("        .vanillaBlock($T.$N, true)\n", VANILLA_BLOCK_ID_CLASS_NAME, vanillaBlockId.name());
        var blockPaletteData = CodeGen.MAPPED_BLOCK_PALETTE_NBT.get(vanillaBlockId.getIdentifier().toString());
        var states = blockPaletteData.getCompound("states");
        if (states.size() != 0) {
            initializer.add("        .withProperties(");
            AtomicInteger count = new AtomicInteger();
            states.forEach((name, value) -> {
                var propertyName = BLOCK_PROPERTY_TYPE_INFO_FILE.differentSizePropertyTypes.contains(name.replaceAll(":", "_")) && BLOCK_PROPERTY_TYPE_INFO_FILE.specialBlockTypes.containsKey(vanillaBlockId.getIdentifier().toString()) ?
                        BLOCK_PROPERTY_TYPE_INFO_FILE.specialBlockTypes.get(vanillaBlockId.getIdentifier().toString()).get(name.replaceAll(":", "_")).toUpperCase() : name.replaceAll(":", "_").toUpperCase();
                initializer.add("$T.$N" + (states.size() == count.incrementAndGet() ? "" : ", "), VANILLA_BLOCK_PROPERTY_TYPES_CLASS_NAME, propertyName);
            });
            initializer.add(")\n");
        }
        initializer.add("        .addBasicComponents()\n");
        initializer.add("        .build()");
        TYPES_CLASS.addField(
                FieldSpec
                        .builder(ParameterizedTypeName.get(BLOCK_TYPE_CLASS_NAME, className), vanillaBlockId.name() + "_TYPE")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer(initializer.build())
                        .build());
    }
}
