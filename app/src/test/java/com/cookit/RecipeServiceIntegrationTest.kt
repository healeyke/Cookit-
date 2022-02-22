package com.cookit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cookit.dto.Recipe
import com.cookit.service.RecipeService
import com.cookit.service.RecipeServiceStub
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

    lateinit var recipeService : RecipeServiceStub
    var allRecipes : Set<Recipe>? = HashSet<Recipe>()


    @Test
    fun `Given recipe data is available When I search for American Then I should receive PBJ` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainPBJ()
    }

    @Test
    fun `Given recipe data is available When I search for Dinner Then I should receive Hot Pot` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainHotPot()
    }

    @Test
    fun `Given recipe data is available When I search for Italian Then I should receive Lasagna` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainLasagna()
    }

    @Test
    fun `Given recipe data is available When I search for English Then I should receive Banana Bread` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainBananaBread()
    }


    //
//    @Test
//    fun `Given recipe data is available When I search for Vegetarian Then I should receive Veggie Lasagna` () = runTest {
//        givenRecipeServiceIsInitialized()
//        whenRecipeDataIsParsed()
//        thenRecipeCollectionShouldContainVeggieLasagna()
//    }

    private fun givenRecipeServiceIsInitialized() {
        recipeService = RecipeServiceStub()
    }

    private suspend fun whenRecipeDataIsParsed() {
        allRecipes = recipeService.fetchRecipes()
    }

    private fun thenRecipeCollectionShouldContainPBJ() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsPBJ = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("American")) && it.name.equals("PBJ")) {
                containsPBJ = true
            }
        }
        assertTrue(containsPBJ)
    }

    private fun thenRecipeCollectionShouldContainHotPot() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsHotPot = false
        allRecipes!!.forEach {
            if (it.category.equals(("Dinner")) && it.name.equals("Hot Pot")) {
                containsHotPot = true
            }
        }
        assertTrue(containsHotPot)
    }

    private fun thenRecipeCollectionShouldContainLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsLasagna = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("Italian")) && it.name.equals("Lasagna")) {
                containsLasagna = true
            }
        }
        assertTrue(containsLasagna)
    }

    private fun thenRecipeCollectionShouldContainBananaBread() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsBananaBread = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("English")) && it.name.equals("Banana Bread")) {
                containsBananaBread = true
            }
        }
        assertTrue(containsBananaBread)
    }

    //    private fun thenRecipeCollectionShouldContainVeggieLasagna() {
//        assertNotNull(allRecipes)
//        assertTrue(allRecipes!!.isNotEmpty())
//        var containsVeggieLasagna = false
//        allRecipes!!.forEach {
//            if (it.cookingMethods.equals(("Baking")) && it.name.equals("Veggie Lasagna") && it.nutrition.equals(
//                    "Vegetarian"
//                )
//            ) {
//                containsVeggieLasagna = true
//            }
//        }
//        assertTrue(containsVeggieLasagna)
//    }


}