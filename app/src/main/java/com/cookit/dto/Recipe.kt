package com.cookit.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("idMeal")var recipeID : String,
    @SerializedName("strMeal")var name : String,
    @SerializedName("strCategory")var category : String = "",
    @SerializedName("strArea")var cuisine : String = "",
    @SerializedName("strInstructions")var instructions : String = "",
    @SerializedName("strMealThumb")var imageURL : String = "",
    @Expose var ingredients : MutableMap<String, String> = LinkedHashMap()
    ) {
    override fun toString(): String {
        return name
    }
}

data class RecipeList(@SerializedName("meals") var recipes : ArrayList<Recipe> = arrayListOf())

