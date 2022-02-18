package com.cookit.service

import com.cookit.dto.Recipe

class RecipeServiceStub {

    suspend fun fetchRecipes(): Set<Recipe>? {

        var fakeRecipe = mutableSetOf<Recipe>()

         fakeRecipe.add(Recipe(
          "Hot Pot",
         "Boil water",
             "Dinner",
             "Chinese",
             mutableMapOf("Water" to "10 cups", "Spices" to "5 grams", "Beef" to "6 oz")
         ))
        fakeRecipe.add(Recipe(
            "PBJ",
            "Get bread, put jelly and peanut butter on bread",
            "Vegan",
            "American",
            mutableMapOf("Bread" to "2 slices", "Jelly" to "1 tsp", "Peanut Butter" to "2 tsp")
        ))
        fakeRecipe.add(Recipe(
            "Chicken Alfredo",
            "Cook chicken, boil noodles, add alfredo sauce",
            "Dinner",
            "Italian",
            mutableMapOf("Chicken" to "6 oz", "Noodles" to "6 oz", "Sauce" to "6 oz")
        ))
        return fakeRecipe
    }

    }