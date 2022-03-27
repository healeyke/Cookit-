package com.cookit.service

import com.cookit.dto.Recipe

/**
 *  A service class to simulate data.
 *
 *  This class is a test alternative to the [RecipeService] class.
 *  It generates data to be used during testing using [fetchRecipes].
 */
internal class RecipeServiceStub {

    /**
     * Alternative to [RecipeService.fetchRecipes]
     *
     * Generates sample data to be used during testing.
     * @return A sample dataset.
     */
    fun fetchRecipes(): Set<Recipe> {

        return mutableSetOf<Recipe>(
            Recipe(
                "001",
                "Hot Pot",
                "Boil water",
                "Dinner",
                "Chinese",
                "",
                mutableMapOf("Water" to "10 cups", "Spices" to "5 grams", "Beef" to "6 oz")
            ),
            Recipe(
                "002",
                "PBJ",
                "Get bread, put jelly and peanut butter on bread",
                "Vegan",
                "American",
                "",
                mutableMapOf("Bread" to "2 slices", "Jelly" to "1 tsp", "Peanut Butter" to "2 tsp")
            ),
            Recipe(
                "003",
                "Chicken Alfredo",
                "Cook chicken, boil noodles, add alfredo sauce",
                "Dinner",
                "Italian",
                "",
                mutableMapOf("Chicken" to "6 oz", "Noodles" to "6 oz", "Sauce" to "6 oz")
            ),
            Recipe(
                "004",
                "Lasagna",
                "Bake Lasagna for 45 mins",
                "Dinner",
                "Italian",
                "",
                mutableMapOf("Beef" to "6 oz", "Pasta" to "6 oz", "Sauce" to "6 oz")
            ),
            Recipe(
                "005",
                "Veggie Lasagna",
                "Bake Lasagna for 45 mins",
                "Dinner",
                "Italian",
                "",
                mutableMapOf("Mixed Veggies" to "6 oz", "Noodles" to "6 oz", "Sauce" to "6 oz")
            ),
            Recipe(
                "006",
                "Banana Bread",
                "Put bananas in batter and bake for 30 minutes",
                "Lunch",
                "English",
                "",
                mutableMapOf("Bananas" to "1 cup", "Pecans" to "1 tbsp", "flour" to "1 cup")
            ),
            Recipe(
                "007",
                "HongShao Pork",
                "Wash the pork, marinate it and put it into a pot with cold water. Add seasoning and soy sauce, boil the water over high heat, and then cook over medium low heat for half an hour.",
                "Dinner",
                "Chinese",
                "",
                mutableMapOf(
                    "Pork" to "1 lb",
                    "soy sauce" to "2 spoons",
                    "oyster sauce" to "1 spoons",
                    "green onion" to "1 oz",
                    "ginger" to "1 oz",
                    "anise" to "1",
                    "Chinese prickly ash" to "7",
                    "sugar" to "12 g"
                )
            ),
            Recipe(
                "008",
                "Fried Yum",
                "Wash yams and carrots and slice them. Pour vegetable oil into the pot, add shallots, stir fry for a few seconds, add yam and carrot, and finally add 2g salt, 6g sugar and two tablespoons of vinegar.",
                "Lunch",
                "Chinese",
                "",
                mutableMapOf(
                    "Yam" to "200 g",
                    "Carrot" to "150 g",
                    "shallot" to "1",
                    "salt" to "2g",
                    "sugar" to "6g",
                    "vinegar" to "2 tablespoons"
                )
            )
        )
    }

}