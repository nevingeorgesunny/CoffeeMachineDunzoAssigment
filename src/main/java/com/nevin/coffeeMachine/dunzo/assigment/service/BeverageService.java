package com.nevin.coffeeMachine.dunzo.assigment.service;

import com.nevin.coffeeMachine.dunzo.assigment.objects.context.Beverage;
import lombok.SneakyThrows;

public class BeverageService implements Runnable {

    private Beverage beverage;

     BeverageService(Beverage beverage) {
        this.beverage = beverage;
    }

    @SneakyThrows
    @Override
    public void run() {
        IngredientsInventoryService.getInstance().makeBeverage(beverage);
    }

}
