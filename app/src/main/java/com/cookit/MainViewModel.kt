package com.cookit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookit.dto.Recipe
import com.cookit.service.IRecipeService
import com.cookit.service.RecipeService
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

/**
 * Class for the primary viewmodel
 * Used to supply [MutableLiveData] of type [ArrayList] of Recipe to views
 */
class MainViewModel(var recipeService: IRecipeService = RecipeService()) : ViewModel() {
    val recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()
    var user: User? = null
    var selectedRecipe by mutableStateOf(Recipe())

    fun fetchRecipes() {
        viewModelScope.launch {
            var innerRecipeList = recipeService.fetchRecipes()
            var innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }
    fun saveRecipes()
    {
        user?.let {
            user ->
            val document = if (selectedRecipe.recipeID == null || selectedRecipe.recipeID.isEmpty()){
                // Insert
                firestore.collection("users").document(user.uid).collection("recipes").document()
            }
            else
            {
                // Update
                firestore.collection("users").document(user.uid).collection("recipes").document(selectedRecipe.recipeID)

            }
            selectedRecipe.recipeID = document.id
            val handle = document.set(selectedRecipe)
            handle.addOnSuccessListener{log.d("Firebase", "Document Saved")}
        }

    }

}