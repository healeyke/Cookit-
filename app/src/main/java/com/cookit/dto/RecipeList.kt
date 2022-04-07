package com.cookit.dto

import com.google.gson.annotations.SerializedName

data class RecipeList(@SerializedName("meals") var recipes : ArrayList<Recipe> = arrayListOf()) {}
