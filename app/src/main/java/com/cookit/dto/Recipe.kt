package com.cookit.dto

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("idMeal")var recipeID : String,
    @SerializedName("strMeal")var name : String,
    @SerializedName("strCategory")var category : String = "",
    @SerializedName("strArea")var cuisine : String = "",
    @SerializedName("strInstructions")var instructions : String = "",
    @SerializedName("strMealThumb")var imageURL : String = "",
    var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>()
    ) {
    override fun toString(): String {
        return name
    }
}
