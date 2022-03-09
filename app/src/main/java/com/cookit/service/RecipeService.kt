package com.cookit.service

import com.cookit.RetrofitClientInstance
import com.cookit.dao.IRecipeDAO
import com.cookit.dto.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IRecipeService {
    suspend fun fetchRecipes() : Set<Recipe>?
}

class RecipeService : IRecipeService {
    override suspend fun fetchRecipes(): Set<Recipe>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IRecipeDAO::class.java)
            val recipes = async { service?.getAllRecipes() }
            var result = recipes.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}