package com.cookit.service

import com.cookit.dto.Recipe

class RecipeServiceStub {

    // service stub of simulated data used in testing

    suspend fun fetchRecipes(): Set<Recipe>? {

        var fakeRecipe = mutableSetOf<Recipe>()

         fakeRecipe.add(Recipe(
            "Hot Pot",
           instructions =  arrayOf("Boil water"),
             "Dinner",
             "Chinese",
             mutableMapOf("Water" to "10 cups", "Spices" to "5 grams", "Beef" to "6 oz")
         ))
        fakeRecipe.add(Recipe(
            "PBJ",
            instructions = arrayOf("Get bread", "put jelly and peanut butter on bread"),
            "Vegan",
            "American",
            mutableMapOf("Bread" to "2 slices", "Jelly" to "1 tsp", "Peanut Butter" to "2 tsp")
        ))
        fakeRecipe.add(Recipe(
            "Chicken Alfredo",
            instructions = arrayOf("Cook chicken", "boil noodles", "add alfredo sauce"),
            "Dinner",
            "Italian",
            mutableMapOf("Chicken" to "6 oz", "Noodles" to "6 oz", "Sauce" to "6 oz")
        ))
        fakeRecipe.add(Recipe(
            "Lasagna",
            instructions = arrayOf("Bake Lasagna for 45 mins"),
            "Dinner",
            "Italian",
            mutableMapOf("Beef" to "6 oz", "Pasta" to "6 oz", "Sauce" to "6 oz")
        ))
        fakeRecipe.add(Recipe(
            "Veggie Lasagna",
            arrayOf("Bake Lasagna for 45 mins"),
            "Dinner",
            "Italian",
            mutableMapOf("Mixed Veggies" to "6 oz", "Noodles" to "6 oz", "Sauce" to "6 oz")
        ))
        fakeRecipe.add(Recipe(
            "Banana Bread",
            arrayOf("Put bananas in batter",  "bake for 30 minutes"),
            "Lunch",
            "English",
            mutableMapOf("Bananas" to "1 cup", "Pecans" to "1 tbsp", "flour" to "1 cup")
        ))
        fakeRecipe.add(Recipe(
            "HongShao Pork",
            arrayOf("Wash the pork, marinate it and put it into a pot with cold water.", "Add seasoning and soy sauce, boil the water over high heat", " then cook over medium low heat for half an hour."),
            "Dinner",
            "Chinese",
            mutableMapOf("Pork" to "1 lb", "soy sauce" to "2 spoons", "oyster sauce" to "1 spoons", "green onion" to "1 oz","ginger" to "1 oz", "anise" to "1","Chinese prickly ash" to "7","sugar" to "12 g")
        ))
        fakeRecipe.add(Recipe(
            "Fried Yum",
            arrayOf("Wash yams and carrots and slice them.", "Pour vegetable oil into the pot, add shallots, stir fry for a few seconds", "add yam and carrot, and finally add 2g salt, 6g sugar and two tablespoons of vinegar."),
            "Lunch",
            "Chinese",
            mutableMapOf("Yam" to "200 g", "Carrot" to "150 g", "shallot" to "1","salt" to "2g","sugar" to "6g","vinegar" to "2 tablespoons")
        ))
        return fakeRecipe
    }

    }