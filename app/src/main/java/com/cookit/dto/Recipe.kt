package com.cookit.dto

import java.util.*
import kotlin.collections.LinkedHashMap

data class Recipe(
    var name : String,
    var instructions : Array<String> = emptyArray(),
    var category : String = "",
    var cuisine : String = "",
    var ingredients : MutableMap<String, String> = LinkedHashMap<String, String>(),
    var id : String ="",
    var tags : Array<String> = emptyArray()
    ) {
    override fun toString(): String {
        return name
    }
}
