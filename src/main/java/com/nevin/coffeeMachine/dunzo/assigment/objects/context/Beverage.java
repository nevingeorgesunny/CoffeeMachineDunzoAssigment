package com.nevin.coffeeMachine.dunzo.assigment.objects.context;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class Beverage {
    private String name;
    private HashMap<String, Integer> ingredients;
}
