package com.cookit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cookit.dto.Recipe
import com.cookit.service.RecipeService
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlinx.coroutines.test.runTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RecipeServiceIntegrationTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var recipeService : RecipeService
    var allRecipes : Set<Recipe>? = HashSet<Recipe>()


    @Test
    fun `Given recipe data is available When I search for Japanese Then I should receive Tonkotsu Ramen` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainTonkotsuRamen()
    }

    @Test
    fun `Given recipe data is available When I search for Baking then I should receive Lasagna` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainLasagna()
    }

    @Test
    fun `Given recipe data is available When I search for Vegetarian Then I should receive Veggie Lasagna` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainVeggieLasagna()
    }

    private fun givenRecipeServiceIsInitialized() {
        recipeService = RecipeService()
    }

    private suspend fun whenRecipeDataIsParsed() {
        allRecipes = recipeService.fetchRecipes()
    }

    private fun thenRecipeCollectionShouldContainTonkotsuRamen() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsTonkotsuRamen = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("Japanese")) && it.name.equals("Tonkotsu Ramen")) {
                containsTonkotsuRamen = true
            }
        }
        assertTrue(containsTonkotsuRamen)
    }

    private fun thenRecipeCollectionShouldContainLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsLasagna = false
        allRecipes!!.forEach {
            if (it.cookingMethods.equals(("Baking")) && it.name.equals("Lasagna")) {
                containsLasagna = true
            }
        }
        assertTrue(containsLasagna)
    }

    private fun thenRecipeCollectionShouldContainVeggieLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsVeggieLasagna = false
        allRecipes!!.forEach {
            if (it.cookingMethods.equals(("Baking")) && it.name.equals("Veggie Lasagna") && it.nutrition.equals("Vegetarian")) {
                containsVeggieLasagna = true
            }
        }
        assertTrue(containsVeggieLasagna)
    }

}