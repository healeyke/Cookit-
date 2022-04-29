package com.cookit.dto

/**
 * A representation of a meal.
 *
 * This is the data class for a Meal object
 *
 * @property recipeID The ID for recipe document on firebase
 * @property name Name of the meal
 * @property mealID The ID for the meal document on firebase
 * @property description Detailed description of the meal
 * @property image Image of the meal
 */
data class Meal(
    var recipeID: String,
    var name: String,
    var mealID: String,
    var description: String,
    var image: String
)
{
    override fun toString(): String {
        return name
    }
}
