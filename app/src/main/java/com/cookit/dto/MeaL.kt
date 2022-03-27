package com.cookit.dto

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idRecipe")var recipeID: String,
    @SerializedName("strRecipe")var name: String,
    @SerializedName("idMeal")var mealId: String,
    @SerializedName("strDescription")var description: String,
    @SerializedName("imageURL")var image: String) {
    override fun toString(): String {
        return name
    }
}
