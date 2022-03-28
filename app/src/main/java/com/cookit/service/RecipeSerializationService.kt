package com.cookit.service

import com.cookit.dto.Recipe
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * A service class that provides custom deserialization logic to retrofit
 *
 * @Implements the [JsonDeserializer] interface
 * @returns a [Recipe] by looping through the json to put the ingredient information into a [MutableMap]
 */
class RecipeSerializationService : JsonDeserializer<Recipe> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Recipe {
        val inputJSON = json.asJsonObject

        var ingredientMap : MutableMap<String, String> = mutableMapOf()

        for (i in 1..20) {
            if (!inputJSON.get("strIngredient$i").isJsonNull) {
                ingredientMap[inputJSON.get("strIngredient$i").asString] = inputJSON.get("strMeasure$i").asString
            }
        }

        return Recipe(
            inputJSON.get("idMeal").asString,
            inputJSON.get("strMeal").asString,
            inputJSON.get("strCategory").asString,
            inputJSON.get("strArea").asString,
            inputJSON.get("strInstructions").asString,
            inputJSON.get("strMealThumb").asString,
            ingredientMap
        )
    }
}