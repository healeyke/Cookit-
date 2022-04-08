package com.cookit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookit.dto.Meal
import com.cookit.dto.Recipe
import com.cookit.service.IRecipeService
import com.cookit.service.RecipeService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.launch

/**
 * Class for the primary viewmodel
 * Used to supply [MutableLiveData] of type [ArrayList] of [Recipe] to views
 */
class MainViewModel(var recipeService: IRecipeService = RecipeService()) : ViewModel() {

    val recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    internal fun fetchRecipes() {
        viewModelScope.launch {
            val innerRecipeList = recipeService.fetchRecipes()
            val innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }

    fun save(meal: Meal) {
        val document = if (meal.mealID == null || meal.mealID.isEmpty()) {
            // create a new meal
            firestore.collection("meals").document()
        } else {
            // update an existing meal.
            firestore.collection("meals").document(meal.mealID)
        }
        meal.mealID = document.id
        val handle = document.set(meal)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
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
