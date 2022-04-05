package com.cookit

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

    fun fetchRecipes() {
        viewModelScope.launch {
            var innerRecipeList = recipeService.fetchRecipes()
            var innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }

}