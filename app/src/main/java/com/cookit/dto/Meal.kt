package com.cookit.dto

data class Meal(
    var recipeID: String,
    var name: String,
    var mealID: String,
    var description: String,
    var image: String
) {
    override fun toString(): String {
        return name
    }
}
