package com.nevin.coffeeMachine.dunzo.assigment.objects.context;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Ingredients {
    private String name;
    private int quantity;
}
