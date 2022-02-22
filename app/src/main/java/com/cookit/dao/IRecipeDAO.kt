package com.cookit.dao

import com.cookit.dto.Recipe
import retrofit2.Call
import retrofit2.http.GET

interface IRecipeDAO {
    @GET("")
    fun getAllRecipes() : Call<Set<Recipe>>
}