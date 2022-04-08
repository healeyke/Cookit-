package com.cookit.dto

data class Meal(
    var recipeID: String = "",
    var mealID: String = "",
    var recipeName: String = "",
    var recipeCategory: String = "",
    var recipeCuisine: String = "",
    var recipeIngredients: String = "",
    var recipeInstructions: String = "",
) {
    override fun toString(): String {
        return recipeName
    }
}
