package com.cookit.service

/**
 * Ingredient mapping service interface that has 2 functions
 * [stringToMap] converts a string into a map
 * [mapToString] converts a map to a string
 */
interface IIngredientMapService {
    fun stringToMap(input: String): MutableMap<String, String>
    fun mapToString(input: MutableMap<String, String>): String
}

/**
 * This implementation of the [IIngredientMapService] converts strings and maps based on the
 * format expected from a text field in our UI.
 * The format looks like this:
 * ingredient=amount,\ningredient2=amount,\ningredient3=amount,\n ... ingredient20=amount
 * \n is converted into a new line in the text field
 */
class TextFieldIngredientMapService : IIngredientMapService {
    override fun stringToMap(input: String): MutableMap<String, String> {
        val outputMap = input.split(",\n").associate {
            val (left, right) = it.trim().split("=")
            left to right
        }.toMutableMap()
        return outputMap
    }

    override fun mapToString(input: MutableMap<String, String>): String {
        return input.entries.joinToString(limit = 20, separator = ",\n")
    }
}