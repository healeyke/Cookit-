package com.cookit.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * A class to represent recipes
 *
 * This class serializes the JSON from IRecipeDAO and parses it into a kotlin object
 * @property recipeID
 * @property name Name of the recipe
 * @property category Category of the recipe
 * @property cuisine Cuisine of the recipe
 * @property instructions Instructions of how to create the recipe
 * @property imageURL URL of the image associated with the recipe
 */
data class Recipe(
    @SerializedName("idMeal")var recipeID : String = "",
    @SerializedName("strMeal")var name : String = "",
    @SerializedName("strCategory")var category : String = "",
    @SerializedName("strArea")var cuisine : String = "",
    @SerializedName("strInstructions")var instructions : String = "",
    @SerializedName("strMealThumb")var imageURL : String = "",
    @SerializedName("strYoutube")var youtubeURL : String = "",
    @Expose var ingredients : MutableMap<String, String> = LinkedHashMap(),
    @Transient var fireStoreID: String = "",
    ) {
    override fun toString(): String {
        return name
    }
}


