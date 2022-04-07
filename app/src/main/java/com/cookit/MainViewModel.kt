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
 * Used to supply [MutableLiveData] of type [ArrayList] of Recipe to views
 */
class MainViewModel(var recipeService: IRecipeService = RecipeService()) : ViewModel() {
    val recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()

    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            var innerRecipeList = recipeService.fetchRecipes()
            var innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }

    fun save(meal : Meal) {
        val document = if (meal.mealID == null || meal.mealID.isEmpty()) {
            // create a new specimen
            firestore.collection("meals").document()
        } else {
            // update an existing specimen.
            firestore.collection("meals").document(meal.mealID)
        }
        meal.mealID = document.id
        val handle = document.set(meal)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ")}
    }
}