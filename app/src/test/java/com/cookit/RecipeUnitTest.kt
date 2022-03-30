package com.cookit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cookit.dto.Recipe
import com.cookit.dto.RecipeList
import com.cookit.service.RecipeService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RecipeUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mvm: MainViewModel

    @MockK
    lateinit var mockRecipeService: RecipeService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a ViewModel with LiveData when populated with recipes then should return HongShao Pork`() {
        givenViewModelIsInitializedWithMockData()
        whenRecipeServiceFetchRecipesInvoked()
        thenResultsShouldContainHongShaoPork()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        var recipeList = RecipeList()
        recipeList.recipes.add(
            Recipe(
                "007",
                "HongShao Pork",
                "Dinner",
                "Chinese",
                "Wash the pork, marinate it and put it into a pot with cold water. Add seasoning and soy sauce, boil the water over high heat, and then cook over medium low heat for half an hour.",
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
            )
        )
        recipeList.recipes.add(
            Recipe(
                "008",
                "Fried Yum",
                "Lunch",
                "Chinese",
                "Wash yams and carrots and slice them. Pour vegetable oil into the pot, add shallots, stir fry for a few seconds, add yam and carrot, and finally add 2g salt, 6g sugar and two tablespoons of vinegar.",
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
        recipeList.recipes.add(
            Recipe(
                "006",
                "Banana Bread",
                "Lunch",
                "English",
                "Put bananas in batter and bake for 30 minutes",
                "",
                mutableMapOf("Bananas" to "1 cup", "Pecans" to "1 tbsp", "flour" to "1 cup")
            )
        )

        coEvery { mockRecipeService.fetchRecipes() } returns recipeList

        mvm.recipeService = mockRecipeService
    }

    private fun whenRecipeServiceFetchRecipesInvoked() {
        mvm.fetchRecipes()
    }

    private fun thenResultsShouldContainHongShaoPork() {
        var allRecipes: RecipeList = RecipeList()

        val latch = CountDownLatch(1)
        val observer = object : Observer<ArrayList<Recipe>> {
            override fun onChanged(receivedRecipes: ArrayList<Recipe>?) {
                allRecipes.recipes = receivedRecipes.let {
                    receivedRecipes
                } ?: ArrayList<Recipe>()
                latch.countDown()
                mvm.plants.removeObserver(this)
            }

        }

        mvm.plants.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allRecipes)
        assertTrue(allRecipes.recipes!!.isNotEmpty())
        var containsHongShaoPork = false
        allRecipes.recipes!!.forEach {
            if (it.name.equals("HongShao Pork") && it.cuisine.equals("Chinese")) {
                containsHongShaoPork = true
            }
        }

        assertTrue(containsHongShaoPork)
    }

}