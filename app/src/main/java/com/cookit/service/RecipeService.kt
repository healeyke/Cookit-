package com.cookit.service

import com.cookit.RetrofitClientInstance
import com.cookit.dao.IRecipeDAO
import com.cookit.dto.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 *  A service class related to Recipes.
 */
class RecipeService {
    /**
     * Gets Recipe from the database.
     *
     * This function implements the [IRecipeDAO] interface to fetch recipe data from the specified url.
     * @throws Exception Throws an exception when the server response is not successful.
     * @return Response body from the url specified at [IRecipeDAO.getAllRecipes] GET wrapper
     */
interface IRecipeService {
    suspend fun fetchRecipes() : Set<Recipe>?
}

class RecipeService : IRecipeService {
    override suspend fun fetchRecipes(): Set<Recipe>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IRecipeDAO::class.java)
            val response = service!!.getAllRecipes().awaitResponse()
            if (response.isSuccessful)
            {
                return@withContext response.body()
            }else{
                throw Exception("Failed to get recipes. Server Response: ${response.code()}")
            }
        }
    }
}