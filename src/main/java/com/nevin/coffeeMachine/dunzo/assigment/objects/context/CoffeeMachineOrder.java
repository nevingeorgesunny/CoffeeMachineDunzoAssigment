package com.nevin.coffeeMachine.dunzo.assigment.objects.context;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CoffeeMachineOrder {
    Map<String ,Integer> order;
}
