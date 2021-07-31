package com.nevin.coffeeMachine.dunzo.assigment.service;

import com.nevin.coffeeMachine.dunzo.assigment.objects.context.Beverage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * IngredientsInventoryService is used for
 * - Keeping track of the ingredients quantity
 * - reducing the quantity when a beverage is being made
 * - topping up ingredients with the quantity is running low
 */
@Slf4j
public class IngredientsInventoryService {

    private static final int MAX_QUANTITY = 100000;
    private HashMap<String, Integer> ingredientsInventory = new HashMap<>();
    private static IngredientsInventoryService ingredientsInventoryService;

    private IngredientsInventoryService() {
    }

    public static IngredientsInventoryService getInstance() throws Exception {
        if (ingredientsInventoryService == null) {
            ingredientsInventoryService = new IngredientsInventoryService();
        }
        return ingredientsInventoryService;
    }

    /**
     * This Method is synchronized to make it thread safe
     */
    public synchronized void makeBeverage(Beverage beverage) {
        if (checkIfBeverageIsPossibleToMake(beverage)) {
            consumeInventoryAndMakeBeverage(beverage);
        }
    }

    /**
     * This Method is reduces the quantity when a beverage is being made
     */
    @SneakyThrows
    private void consumeInventoryAndMakeBeverage(Beverage beverage) {
        for (String ingredient : beverage.getIngredients().keySet()) {
            Integer ingredientInventory = ingredientsInventory.get(ingredient);
            ingredientsInventory.put(ingredient, ingredientInventory - beverage.getIngredients().get(ingredient));
        }
        System.out.println(beverage.getName() + " is prepared");
    }

    /**
     * This Method is checks if the beverage can be made with the current inventory
     */
    private boolean checkIfBeverageIsPossibleToMake(Beverage beverage) {
        for (String ingredient : beverage.getIngredients().keySet()) {
            Integer ingredientInventory = ingredientsInventory.get(ingredient);

            if (ingredientInventory == null) {
                System.out.println(beverage.getName() + "  cannot be prepared because " + ingredient
                        + " is not available");
                return false;
            }

            if (ingredientInventory >= beverage.getIngredients().get(ingredient)) {
                continue;
            } else {
                System.out.println(beverage.getName() + "  cannot be prepared because " + ingredient
                        + " is not available");
                return false;
            }
        }

        return true;
    }

    /**
     * This Method is for topping up ingredients
     * - it checks if the quantity exceeds MAX_QUANTITY
     * if it does it will show an error message, and only addes the MAX_QUANTITY for the ingredient
     * it does not throw an error because to enhance UX
     */
    public void addInventory(String ingredient, int quantity) {
        int existingQuantity = ingredientsInventory.getOrDefault(ingredient, 0);
        if ((existingQuantity + quantity) > MAX_QUANTITY) {
            System.out.println(ingredient + " exceeds limit . only adding  " + MAX_QUANTITY);
            ingredientsInventory.put(ingredient, MAX_QUANTITY);
            return;
        }

        ingredientsInventory.put(ingredient, existingQuantity + quantity);
    }
}
