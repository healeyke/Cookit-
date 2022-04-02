package com.cookit.dao

import com.cookit.dto.RecipeList
import retrofit2.Call
import retrofit2.http.GET

interface IRecipeDAO {
    @GET("9973533/search.php?f=a")
    fun getAllRecipes() : Call<RecipeList>
}