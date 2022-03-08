package com.cookit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cookit.dto.Recipe
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

    fun searchRecipeByCategory(category: String, name: String): Boolean {
        allRecipes!!.forEach{
            if(it.category.equals(category) && it.name.equals(name)) {
                return true
            }
        }
        return false
    }
    fun searchRecipeByCuisine(cusine: String, name: String) : Boolean {
        allRecipes!!.forEach{
            if(it.cuisine.equals(cusine) && it.name.equals(name)) {
                return true
            }
        }
        return false
    }
    private fun thenRecipeCollectionShouldContainPBJ() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsPBJ = searchRecipeByCuisine("American", "PBJ")
        assertTrue(containsPBJ)
    }

    private fun thenRecipeCollectionShouldContainHotPot() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsHotPot = searchRecipeByCategory("Dinner","Hot Pot")
        assertTrue(containsHotPot)
    }

    private fun thenRecipeCollectionShouldContainVeggieLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsVeggieLasagna = searchRecipeByCategory("Dinner","Veggie Lasagna")
        assertTrue(containsVeggieLasagna)
    }

    private fun thenRecipeCollectionShouldContainLasagna() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsLasagna = searchRecipeByCuisine("Italian","Lasagna")
        assertTrue(containsLasagna)
    }

    private fun thenRecipeCollectionShouldContainBananaBread() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsBananaBread = searchRecipeByCuisine("English","Banana Bread")
        assertTrue(containsBananaBread)
    }

    private fun thenRecipeCollectionShouldContainChickenAlfredo() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsChickenAlfredo = searchRecipeByCuisine("Italian", "Chicken Alfredo")
        assertTrue(containsChickenAlfredo)
    }
    private fun thenRecipeCollectionShouldContainHongShaoPork() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsHongShaoPork = searchRecipeByCuisine("Chinese", "HongShao Pork")
        assertTrue(containsHongShaoPork)
    }
    private fun thenRecipeCollectionShouldContainFriedYum() {
        assertNotNull(allRecipes)
        assertTrue(allRecipes!!.isNotEmpty())
        var containsFriedYum = searchRecipeByCuisine("Chinese","Fried Yum")
        assertTrue(containsFriedYum)
    }


}