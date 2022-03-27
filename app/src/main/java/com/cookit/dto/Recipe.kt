package com.cookit.dto

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("idRecipe")var recipeID : String,
    @SerializedName("strRecipe")var name : String,
    @SerializedName("strCategory")var category : String = "",
    @SerializedName("strArea")var cuisine : String = "",
    @SerializedName("strInstructions")var instructions : String = "",
    @SerializedName("strRecipeThumb")var imageURL : String = "",
    var ingredients : MutableMap<String, String> = LinkedHashMap()
    ) {
    override fun toString(): String {
        return name
    }
}

