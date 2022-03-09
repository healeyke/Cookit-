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
    fun `I receive PBJ an allergen field should be listed as peanuts` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionHasPossibleAllergenFieldReturningPeanuts()
    }

    @Test
    fun `Given recipe data is available When I search for Dinner Then I should receive Hot Pot` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainHotPot()
    }

    @Test
    fun `Given recipe data When I search for Dinner and I receive Hot Pot allergen field is none` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionContainHotPotReturnsNoAllergens()
    }

    @Test
    fun `Given recipe data is available When I search for Dinner Then I should receive Veggie Lasagna` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainVeggieLasagna()
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

    @Test
    fun `Given recipe data is available When I search for Italian Then I should receive Chicken Alfredo` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainChickenAlfredo()
    }

    @Test
    fun `Given recipe data is available When I search for Chinese Then I should receive HongShao Pork` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainHongShaoPork()
    }

    @Test
    fun `Given recipe data is available When I search for Chinese Then I should receive Fried Yum` () = runTest {
        givenRecipeServiceIsInitialized()
        whenRecipeDataIsParsed()
        thenRecipeCollectionShouldContainFriedYum()
    }


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

    private fun thenRecipeCollectionHasPossibleAllergenFieldReturningPeanuts() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsAllergen = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("American")) && it.name.equals("PBJ") && it.possibleAllergens.equals("Peanuts")) {
                containsAllergen = true
            }
        }
        assertTrue(containsAllergen)
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

    private fun thenRecipeCollectionContainHotPotReturnsNoAllergens() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsNoAllergens = false
        allRecipes!!.forEach {
            if (it.possibleAllergens.equals(("none")) && it.name.equals("Hot Pot")) {
                containsNoAllergens = true
            }
        }
        assertTrue(containsNoAllergens)
    }

    private fun thenRecipeCollectionShouldContainVeggieLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsVeggieLasagna = false
        allRecipes!!.forEach {
            if (it.category.equals(("Dinner")) && it.name.equals("Veggie Lasagna")) {
                containsVeggieLasagna = true
            }
        }
        assertTrue(containsVeggieLasagna)
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

    private fun thenRecipeCollectionShouldContainChickenAlfredo() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsChickenAlfredo = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("Italian")) && it.name.equals("Chicken Alfredo")) {
                containsChickenAlfredo = true
            }
        }
        assertTrue(containsChickenAlfredo)
    }
    private fun thenRecipeCollectionShouldContainHongShaoPork() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsHongShaoPork = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("Chinese")) && it.name.equals("HongShao Pork")) {
                containsHongShaoPork = true
            }
        }
        assertTrue(containsHongShaoPork)
    }
    private fun thenRecipeCollectionShouldContainFriedYum() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsFriedYum = false
        allRecipes!!.forEach {
            if (it.cuisine.equals(("Chinese")) && it.name.equals("Fried Yum")) {
                containsFriedYum = true
            }
        }
        assertTrue(containsFriedYum)
    }


}