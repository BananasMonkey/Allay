package org.allaymc.server.container.processor;

import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.container.FullContainerType;
import org.allaymc.api.container.impl.CraftingContainer;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.item.interfaces.ItemAirStack;
import org.allaymc.api.item.type.ItemTypes;
import org.allaymc.api.registry.Registries;
import org.allaymc.server.item.enchantment.EnchantmentOptionGenerator;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ConsumeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.allaymc.api.container.FullContainerType.CREATED_OUTPUT;

/**
 * @author daoge_cmd
 */
@Slf4j
public class CraftRecipeActionProcessor implements ContainerActionProcessor<CraftRecipeAction> {
    public static final String RECIPE_DATA_KEY = "recipe";
//    public static final String IS_ENCHANT_RECIPE_KEY = "is_enchant_recipe";

    @Override
    public ActionResponse handle(CraftRecipeAction action, EntityPlayer player, int currentActionIndex, ItemStackRequestAction[] actions, Map<Object, Object> dataPool) {
        var recipeNetworkId = action.getRecipeNetworkId();
        if (recipeNetworkId >= EnchantmentOptionGenerator.NETWORK_ID_COUNTER_INITIAL_VALUE) {
//            dataPool.put(IS_ENCHANT_RECIPE_KEY, true);
            return handleEnchantTableRecipe(player, recipeNetworkId);
        } else {
            return handleCraftingRecipe(action, player, currentActionIndex, actions, dataPool, recipeNetworkId);
        }
    }

    protected ActionResponse handleEnchantTableRecipe(EntityPlayer player, int recipeNetworkId) {
        var data = EnchantmentOptionGenerator.removeEnchantOption(recipeNetworkId);
        if (data == null) {
            log.warn("Can't find enchant table recipe by network id {}", recipeNetworkId);
            return error();
        }

        var enchantTableContainer = player.getOpenedContainer(FullContainerType.ENCHANT_TABLE);
        var inputItem = enchantTableContainer.getInput();
        if (inputItem.getItemType() == ItemTypes.AIR) {
            log.warn("Input item is air!");
            return error();
        }

        if (player.getGameType() != GameType.CREATIVE) {
            var material = enchantTableContainer.getMaterial();
            if (material.getItemType() != ItemTypes.LAPIS_LAZULI || material.getCount() < data.requiredLapisLazuliCount()) {
                log.warn("Not enough lapis lazuli! Need: {}, Current: {}", data.requiredLapisLazuliCount(), enchantTableContainer.getMaterial().getCount());
                return error();
            }

            if (player.getExperienceLevel() < data.requiredXpLevel()) {
                log.warn("Not enough experience level! Need: {}, Current: {}", data.requiredXpLevel(), player.getExperienceLevel());
                return error();
            }
            player.setExperienceLevel(player.getExperienceLevel() - data.requiredXpLevel());
        }

        var enchantedItem = inputItem.copy(true);
        enchantedItem.addEnchantments(data.enchantments());
        // Copy the enchanted item to CREATED_OUTPUT, and client will send a PlaceAction
        // to move the enchanted item back to the input slot of the enchant table container
        player.getContainer(FullContainerType.CREATED_OUTPUT).setItemStack(0, enchantedItem);
        player.regenerateEnchantmentSeed();

        return null;
    }

    protected ActionResponse handleCraftingRecipe(CraftRecipeAction action, EntityPlayer player, int currentActionIndex, ItemStackRequestAction[] actions, Map<Object, Object> dataPool, int recipeNetworkId) {
        CraftingContainer craftingContainer = player.getOpenedContainer(FullContainerType.CRAFTING_TABLE);
        if (craftingContainer == null) {
            // The player is not opening a crafting table, using crafting grid instead
            craftingContainer = player.getContainer(FullContainerType.CRAFTING_GRID);
        }
        var numberOfRequestedCrafts = action.getNumberOfRequestedCrafts();

        // Validate item type
        var recipe = Registries.RECIPES.get(recipeNetworkId);
        var input = craftingContainer.createCraftingInput();
        var matched = recipe.match(input);
        if (!matched) {
            log.warn("Mismatched recipe! Network id: {}", recipe.getNetworkId());
            return error();
        }

        // Validate if the player has provided enough ingredients
        var itemStackArray = craftingContainer.getItemStackArray();
        for (int slot = 0; slot < itemStackArray.length; slot++) {
            var ingredient = itemStackArray[slot];
            // Skip empty slot because we have checked item type above
            if (ingredient == ItemAirStack.AIR_STACK) continue;
            if (ingredient.getCount() < numberOfRequestedCrafts) {
                log.warn("Not enough ingredients in slot {}! Expected: {}, Actual: {}", slot, numberOfRequestedCrafts, ingredient.getCount());
                return error();
            }
        }

        // Validate the consume action count which client sent
        // Some checks are also placed in ConsumeActionProcessor (e.g., item consumption count check)
        var consumeActions = findAllConsumeActions(actions, currentActionIndex + 1);
        var consumeActionCountNeeded = craftingContainer.calculateShouldConsumedItemSlotCount();
        if (consumeActions.size() != consumeActionCountNeeded) {
            log.warn("Mismatched consume action count! Expected: {}, Actual: {}", consumeActionCountNeeded, consumeActions.size());
            return error();
        }

        if (recipe.getOutputs().length == 1) {
            // If the recipe outputs a single item, the client will not send a CreateAction,
            // so we directly set the output in CREATED_OUTPUT in CraftRecipeAction
            var output = recipe.getOutputs()[0].copy(false);
            output.setCount(output.getCount() * numberOfRequestedCrafts);
            player.getContainer(CREATED_OUTPUT).setItemStack(0, output);
        } else {
            if (numberOfRequestedCrafts != 1) {
                log.warn("Number of requested crafts for multi-outputs recipe should be one! Actual: {}", numberOfRequestedCrafts);
                return error();
            }
            // If the recipe outputs multiple items, the client will send a CreateAction, so we will set the output in CREATED_OUTPUT in CreateActionProcessor
            // Put recipe to data pool so that CreateActionProcessor can get it
            dataPool.put(RECIPE_DATA_KEY, recipe);
        }

        return null;
    }

    @Override
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_RECIPE;
    }

    protected List<ConsumeAction> findAllConsumeActions(ItemStackRequestAction[] actions, int startIndex) {
        return Arrays.stream(actions, startIndex, actions.length)
                .filter(action -> action instanceof ConsumeAction)
                .map(action -> (ConsumeAction) action)
                .toList();
    }
}
