package com.nevin.coffeeMachine.dunzo.assigment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nevin.coffeeMachine.dunzo.assigment.objects.context.Beverage;
import com.nevin.coffeeMachine.dunzo.assigment.objects.context.CoffeeMachineOrder;
import com.nevin.coffeeMachine.dunzo.assigment.objects.context.Ingredients;
import com.nevin.coffeeMachine.dunzo.assigment.objects.model.CoffeeMachine;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 *  This Service is responsible for
 *  - Initializing the CoffeeMachine with the ingredients and recipe provided in the input
 *      - it also creates a ThreadPoolExecutor with thread pool size = the number of outlets
 *        so that at most N beverages can be served parallel
 *      - It also creates IngredientsInventoryService which keeps track of the ingredients quantity available
 *        and also reduces the ingredients when a beverages is created
 *  - It takes in orders of beverages
 *  - It also can top up ingredients if required
 */
public class CoffeeMachineService {

    private static CoffeeMachineService coffeeMachineService;
    public CoffeeMachine coffeeMachine;
    public IngredientsInventoryService inventoryManager;
    private static final int MAX_QUEUED_REQUEST = 100;
    private ThreadPoolExecutor executor;

    private CoffeeMachineService() {
    }

    /**
     * This method expects an input json that contains all the ingredients and recipe
     * with which CoffeeMachineService is initialized
     */
    public static CoffeeMachineService initializeMachine(final String jsonInput) throws Exception {
        if (coffeeMachineService == null) {
            coffeeMachineService = new CoffeeMachineService(jsonInput);
        }
        return coffeeMachineService;
    }

    /**
     * This method takes in the order and processes it
     * for each beverage it calls the makeBeverage method
     */
    public void processOrder(CoffeeMachineOrder coffeeMachineOrder) {
        coffeeMachineOrder.getOrder()
                .forEach((beverageName, count) -> {
                    HashMap<String, Integer> ingredients = coffeeMachine.getMachine().getBeverages().get(beverageName);
                    if (ingredients == null) {
                        System.out.println(beverageName + " cannot be made as no recipe for this Beverage is provided in input");
                    } else {
                        makeBeverage(count, Beverage.builder()
                                .name(beverageName)
                                .ingredients(ingredients)
                                .build());
                    }
                });
    }

    /**
     * This method create a BeverageService Thread object and executes it with ThreadPoolExecutor created in
     * initializeMachine
     */
    private void makeBeverage(Integer count, Beverage beverage) {
        // if there are multiple orders of the same beverage make BeverageService N times
        for (int i = 0; i < count; i++) {
            BeverageService beverageService = new BeverageService(beverage);
            executor.execute(beverageService);
        }
    }

    /**
     * This is used to top up ingredients if any of them are running low
     */
    @SneakyThrows
    public void topUpIngredient(List<Ingredients> ingredients){
        inventoryManager = IngredientsInventoryService.getInstance();
        ingredients.forEach(ingredient -> {
            inventoryManager.addInventory(ingredient.getName(),ingredient.getQuantity());
        });
    }

    /**
     * Called by initializeMachine while CoffeeMachineService is initialized
     */
    @SneakyThrows
    private CoffeeMachineService(String jsonInput) {
        coffeeMachine = readInputJson(jsonInput);
        initializeTreadPool();
        initializeIngredientsInventory();
    }


    /**
     * Used to create the ingredients inventory while CoffeeMachineService is initialized
     */
    private void initializeIngredientsInventory() throws Exception {
        inventoryManager = IngredientsInventoryService.getInstance();
        coffeeMachine.getMachine().getTotalItemsQuantity()
                .forEach((ingredient, quantity) -> {
                    inventoryManager.addInventory(ingredient, quantity);
                });
    }

    /**
     * Used to parse the input JSON
     */
    @SneakyThrows
    private CoffeeMachine readInputJson(String jsonInput) {
        return new ObjectMapper().readValue(jsonInput, CoffeeMachine.class);
    }

    /**
     * Used to create ThreadPoolExecutor with thread pool size = the number of outlets
     * so that at most N beverages can be served parallel
     */
    private void initializeTreadPool() {
        int outlet = coffeeMachine.getMachine().getOutlets().getCountN();
        executor = new ThreadPoolExecutor(outlet, outlet, 5000L
                , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
    }


}
