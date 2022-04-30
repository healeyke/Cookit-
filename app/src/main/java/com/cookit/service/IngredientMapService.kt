package com.cookit.service

interface IIngredientMapService {
    fun stringToMap(input: String): MutableMap<String, String>
    fun mapToString(input: MutableMap<String, String>): String
}

class IngredientMapService : IIngredientMapService {
    override fun stringToMap(input: String): MutableMap<String, String> {
        val outputMap = input.split(",\n").associate {
            val (left, right) = it.split("=")
            left to right
        }.toMutableMap()
        return outputMap
    }

    override fun mapToString(input: MutableMap<String, String>): String {
        return input.entries.joinToString(limit = 20, separator = ",\n")
    }
}