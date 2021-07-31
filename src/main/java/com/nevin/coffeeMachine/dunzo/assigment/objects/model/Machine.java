package com.nevin.coffeeMachine.dunzo.assigment.objects.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * This Class is only used for Parsing the input JSON
 */
@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Machine {
    private Outlets outlets;
    private Map<String, Integer> totalItemsQuantity;
    private HashMap<String, HashMap<String, Integer>> beverages;
}
