package com.cookit

import com.cookit.dto.Recipe
import com.cookit.service.RecipeSerializationService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/"
    // data used for testing located in RecipeServiceStub class
    var recipeDeserializer = RecipeSerializationService()
    var customGSON : Gson = GsonBuilder()
        .registerTypeAdapter(Recipe::class.java, recipeDeserializer)
        .create()

    val retrofitInstance : Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(customGSON))
                    .build()
            }
            return retrofit
        }
}