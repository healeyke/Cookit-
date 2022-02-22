package com.cookit.dto

data class Recipe(
    var name : String,
    var instructions : String = "",
    var category : String = "",
    var cuisine : String = "",
    var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>()
    ) {
    override fun toString(): String {
        return name
    }
}
