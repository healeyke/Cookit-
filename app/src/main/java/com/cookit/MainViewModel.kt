package com.cookit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookit.dto.Recipe
import com.cookit.service.IRecipeService
import com.cookit.service.RecipeService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

/**
 * Class for the primary view model
 * Used to supply [MutableLiveData] to views
 */
class MainViewModel(var recipeService: IRecipeService = RecipeService()) : ViewModel() {

    val recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()
    var selectedRecipe by mutableStateOf(Recipe())
    val NEW_RECIPE = "New Recipe"

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToRecipes()
    }

    private fun listenToRecipes() {
        firestore.collection("recipes").addSnapshotListener {
                snapshot, error ->
            // see of we received an error
            if (error != null) {
                Log.w("listen failed.", error)
                return@addSnapshotListener
            }
            // if we reached this point, there was not an error, and we have data.
            snapshot?.let {
                val allRecipes = ArrayList<Recipe>()
                allRecipes.add(Recipe(NEW_RECIPE))
                val documents = snapshot.documents
                documents.forEach {
                    val recipe = it.toObject(Recipe::class.java)
                    recipe?.let {
                        allRecipes.add(recipe)
                    }
                }
                // we have a populated collection of recipes
                recipes.value = allRecipes
            }
        }
    }

    internal fun fetchRecipes() {
        viewModelScope.launch {
            val innerRecipeList = recipeService.fetchRecipes()
            val innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }

    fun save(recipe: Recipe) {
        val document = if (recipe.fireStoreID.isBlank()) {
            // create a new meal
            firestore.collection("recipes").document()
        } else {
            // update an existing meal.
            firestore.collection("recipes").document(recipe.fireStoreID)
        }
        recipe.fireStoreID = document.id
        val handle = document.set(recipe)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("firebase", "Save failed $it") }
    }

    /**
     * Function that provides a list of predictions to the autocomplete text field
     * @param query is text to search
     * @param take  is the number of results to provide
     * @return [List] of [Recipe] containing any predictions
    */
    fun getPredictionList(query: String, take: Int): List<Recipe> {
        var recipeList = recipes.value
        if (query.isNullOrEmpty()) return listOf()
        recipeList?.let { list ->
            when {
                list.any { it.name.lowercase().startsWith(query) && it.name != query }
                -> {
                    return list.filter {
                        it.name.lowercase().startsWith(query) && it.name != query
                    }.take(take)
                }
                list.any { it.category.lowercase().startsWith(query) }
                -> {
                    return list.filter {
                        it.category.lowercase().startsWith(query)
                    }.take(take)
                }
                list.any { it.cuisine.lowercase().startsWith(query) }
                -> {
                    return list.filter {
                        it.cuisine.lowercase().startsWith(query)
                    }.take(take)
                }
                else -> return listOf()
            }
        }
        return listOf()
    }
}
