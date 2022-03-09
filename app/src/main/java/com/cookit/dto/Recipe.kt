package com.cookit.dto
import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("name")var name : String,
    @SerializedName("instructions")var instructions : String = "",
    @SerializedName("category")var category : String = "",
    @SerializedName("cuisine")var cuisine : String = "",
    @SerializedName("ingredients")var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>()
    ) {
    override fun toString(): String {
        return name
    }
}
