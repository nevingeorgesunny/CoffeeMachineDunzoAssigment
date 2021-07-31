All the test cases are in CoffeeMachineTest.class

CoffeeMachineService is used to Initializing the application
IngredientsInventoryService keeps track of inventory

Please execute
mvn clean install -U
to build the project

Test case :-

All tests cases initialize CoffeeMachineService with the provided input JSON which contains the ingredients and recipe
The order of the customer is taken in with a help of a hashMap of <beverage,quantityOfBeverage>

Output of all the test cases will be printed in the console
if a beverage is made it will print
<beverage> is prepared

if a beverage is not made due to low ingredients, it will print
<beverage>  cannot be prepared because <ingredients> is not available

if not recipe for the provided beverage is set in input it will print
<beverage> cannot be made as no recipe for this Beverage is provided in input

if top up ingredients exceeds limit it will print
<ingredients> exceeds limit . only adding  100000

That said here are few testcase

1.) run
    mvn -Dtest=CoffeeMachineTest#allOrderSatisfied test

    This test case is a happy flow that makes all the beverage

2.) run
    mvn -Dtest=CoffeeMachineTest#someOrderNotSatisfiedDueToLessIngredients test

    This test case is a will only make partial order as the ingredients are running low

3.) run
    mvn -Dtest=CoffeeMachineTest#invalidOrder test

    This test case will show error for one of the input beverage as it is not part of the recipe provided while
    initializing the coffee machine

4.) run
    mvn -Dtest=CoffeeMachineTest#toppingUpInventory test

    This test case will test topping up of inventory. the input json and order is same as test case 2 (someOrderNotSatisfiedDueToLessIngredients)
    but before making the order we are topping up hot_water

5.) run
    mvn -Dtest=CoffeeMachineTest#inputWithExceedingQuantities test

    This will show waring with the input ingredients quantity exceeds limit