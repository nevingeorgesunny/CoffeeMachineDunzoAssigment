package com.nevin.coffeeMachine.dunzo.assigment;

import com.nevin.coffeeMachine.dunzo.assigment.objects.context.CoffeeMachineOrder;
import com.nevin.coffeeMachine.dunzo.assigment.objects.context.Ingredients;
import com.nevin.coffeeMachine.dunzo.assigment.service.CoffeeMachineService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class CoffeeMachineTest {

    CoffeeMachineService coffeeMachineService;

    @SneakyThrows
    public void setUp(String inputFile) {
        File file = new File(CoffeeMachineTest.class.getClassLoader().getResource(inputFile).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.initializeMachine(jsonInput);
    }

    /**
     *
     *     mvn -Dtest=CoffeeMachineTest#allOrderSatisfied test
     *
     *     This test case is a happy flow that makes all the beverage
     */
    @Test
    public void allOrderSatisfied() {
        setUp("input.json");
        Map<String, Integer> order = new HashMap<>();
        order.put("hot_coffee", 1);
        order.put("black_tea", 2);

        CoffeeMachineOrder coffeeMachineOrder = CoffeeMachineOrder.builder()
                .order(order)
                .build();

        coffeeMachineService.processOrder(coffeeMachineOrder);
    }

    /**
     *     mvn -Dtest=CoffeeMachineTest#someOrderNotSatisfiedDueToLessIngredients test
     *
     *     This test case is a will only make partial order as the ingredients are running low
     */
    @Test
    public void someOrderNotSatisfiedDueToLessIngredients() {
        setUp("simple_input.json");
        Map<String, Integer> order = new HashMap<>();
        order.put("green_tea", 4);
        order.put("coffee", 4);

        CoffeeMachineOrder coffeeMachineOrder = CoffeeMachineOrder.builder()
                .order(order)
                .build();

        coffeeMachineService.processOrder(coffeeMachineOrder);
    }

    /**
     *     mvn -Dtest=CoffeeMachineTest#invalidOrder test
     *
     *     This test case will show error for one of the input beverage as it is not part of the recipe provided while
     *     initializing the coffee machine
     */
    @Test
    public void invalidOrder() {
        setUp("simple_input.json");
        Map<String, Integer> order = new HashMap<>();
        order.put("green_tea", 4);
        order.put("coffee", 4);
        order.put("hot_chocolate", 4);

        CoffeeMachineOrder coffeeMachineOrder = CoffeeMachineOrder.builder()
                .order(order)
                .build();

        coffeeMachineService.processOrder(coffeeMachineOrder);
    }

    /**
     *      mvn -Dtest=CoffeeMachineTest#toppingUpInventory test
     *
     *     This test case will test topping up of inventory. the input json and order is same as test case 2 (someOrderNotSatisfiedDueToLessIngredients)
     *     but before making the order we are topping up hot_water
     */
    @Test
    @SneakyThrows
    public void toppingUpInventory() {
        setUp("simple_input.json");

        coffeeMachineService.topUpIngredient(Arrays.asList(Ingredients.builder()
                .name("hot_water")
                .quantity(500)
                .build()));

        Map<String, Integer> order = new HashMap<>();
        order.put("green_tea", 5);
        order.put("coffee", 5);

        CoffeeMachineOrder coffeeMachineOrder = CoffeeMachineOrder.builder()
                .order(order)
                .build();

        coffeeMachineService.processOrder(coffeeMachineOrder);
    }


    /**
     *     mvn -Dtest=CoffeeMachineTest#inputWithExceedingQuantities test
     *
     *     This will show waring with the input ingredients quantity exceeds limit
     */
    @Test
    public void inputWithExceedingQuantities() {
        setUp("exceeded_quantity_input.json");
        Map<String, Integer> order = new HashMap<>();
        order.put("green_tea", 1);
        order.put("coffee", 2);

        CoffeeMachineOrder coffeeMachineOrder = CoffeeMachineOrder.builder()
                .order(order)
                .build();

        coffeeMachineService.processOrder(coffeeMachineOrder);
    }


}
