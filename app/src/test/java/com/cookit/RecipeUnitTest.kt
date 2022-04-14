package com.cookit

import com.cookit.dto.Recipe
import junit.framework.Assert.assertTrue
import org.junit.Test

class RecipeUnitTest {
    @Test
    fun `Given a Recipe for Tonkotsu Ramen Then Cuisine Should be Japanese and Ingredients Should Contain Chashu Pork`() {
        val recipe = Recipe(
            "42",
            "Tonkotsu Ramen",
            "Dinner",
            "Japanese",
            "Boil noodles for 3-5 minutes, add tare and soup to the serving bowl in the mean time, strain noodles and place in serving bowl, add chashu pork and other toppings, enjoy!",
            "",
            mutableMapOf(
                "Chashu Pork" to "2 cuts",
                "Soup base" to "300ml",
                "Tare" to "30ml",
                "Ramen Noodles" to "130g"
            )
        )
        assertTrue(recipe.cuisine == "Japanese")
        assertTrue(recipe.ingredients.containsKey("Chashu Pork"))
    }
}