package com.cookit.dto

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName ("Name") var name : String,
    @SerializedName ("Instructions")var instructions : String = "",
    @SerializedName ("Category")var category : String = "",
    @SerializedName ("Cuisine")var cuisine : String = "",
    @SerializedName ("possibleAllergens")var possibleAllergens : String = "",
    @SerializedName ("Ingredients")var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>()
    ) {
    override fun toString(): String {
        return name
    }
}
