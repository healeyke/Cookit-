package com.cookit.service

import com.cookit.RetrofitClientInstance
import com.cookit.dao.IRecipeDAO
import com.cookit.dto.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.awaitResponse


class RecipeService {
    internal suspend fun fetchRecipes(): Set<Recipe>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IRecipeDAO::class.java)
            val recipes = async { service?.getAllRecipes() }

            //updateRecipes(recipes.await())
            return@withContext recipes.await()?.awaitResponse()?.body()
        }
    }

    /*private suspend fun updateRecipes(recipes: Call<Set<Recipe>>?) {

    }*/

}