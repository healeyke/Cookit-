package com.cookit.dto

import com.google.gson.annotations.SerializedName
import androidx.room.PrimaryKey

data class Recipe(
    var name : String,
    var instructions : String = "",
    var category : String = "",
    var cuisine : String = "",
    var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>(),
    @PrimaryKey @SerializedName("id") var recipeId:Int = 0
    ) {
    override fun toString(): String {
        return name
    }
}
