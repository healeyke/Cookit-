package com.cookit.dto

data class Recipe(
    var name : String,
    var description : String = "",
    var cuisine : String = "",
    var ingredients : Set<String> = HashSet<String>(),
    var nutrition : List<String> = ArrayList<String>(),
    var prepTime : Int = 0,
    var cookTime : Int = 0,
    var cookingMethods : Set<String> = HashSet<String>()
    ) {
    override fun toString(): String {
        return name
    }
}
