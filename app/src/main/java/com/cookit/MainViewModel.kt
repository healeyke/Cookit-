package com.cookit

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookit.dto.Photo
import com.cookit.dto.Recipe
import com.cookit.dto.User
import com.cookit.service.IRecipeService
import com.cookit.service.RecipeService
import com.cookit.service.TextFieldIngredientMapService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

/**
 * Class for the primary view model
 * Used to supply [MutableLiveData] to views
 */
class MainViewModel(var recipeService: IRecipeService = RecipeService()) : ViewModel() {

    val photos: ArrayList<Photo> = ArrayList()
    var user: User? = null
    val recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()
    val userRecipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>()
    var selectedRecipe by mutableStateOf(Recipe())
    val NEW_RECIPE = "New Recipe"
    val ingredientMapper = TextFieldIngredientMapService()

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance().reference

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    internal fun listenToRecipes() {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("recipes")
                .addSnapshotListener { snapshot, error ->
                    // see of we received an error
                    if (error != null) {
                        Log.w("listen failed.", error)
                        return@addSnapshotListener
                    }
                    // if we reached this point, there was not an error, and we have data.
                    snapshot?.let {
                        val allRecipes = ArrayList<Recipe>()
                        allRecipes.add(Recipe(name = NEW_RECIPE))
                        val documents = snapshot.documents
                        documents.forEach {
                            val recipe = it.toObject(Recipe::class.java)
                            recipe?.let {
                                allRecipes.add(recipe)
                            }
                        }
                        // we have a populated collection of recipes
                        userRecipes.postValue(allRecipes)
                    }
                }
        }
    }

    /**
     * Fetches recipes from IRecipeDAO and adds it to an arraylist
     */
    internal fun fetchRecipes() {
        viewModelScope.launch {
            val innerRecipeList = recipeService.fetchRecipes()
            val innerRecipes: ArrayList<Recipe> = innerRecipeList?.recipes ?: ArrayList()
            recipes.postValue(innerRecipes)
        }
    }

    fun saveRecipe() {
        user?.let { user ->
            val document = if (selectedRecipe.fireStoreID.isBlank()) {
                // create a new meal
                firestore.collection("users").document(user.uid).collection("recipes").document()
            } else {
                // update an existing meal.
                firestore.collection("users").document(user.uid).collection("recipes")
                    .document(selectedRecipe.fireStoreID)
            }
            selectedRecipe.fireStoreID = document.id
            val handle = document.set(selectedRecipe)
            handle.addOnSuccessListener {
                Log.d("Firebase", "Document Saved")
                if (photos.isEmpty()) {
                    uploadPhotos()
                }
            }
            handle.addOnFailureListener { Log.e("firebase", "Save failed $it") }
        }
    }

    private fun uploadPhotos() {
        photos.forEach { photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Upload $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo) {
        user?.let { user ->
            var photoCollection =
                firestore.collection("users").document(user.uid).collection("recipes")
                    .document(selectedRecipe.fireStoreID).collection("photos")
            var handle = photoCollection.add(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
                photo.id = it.id
                firestore.collection("users").document(user.uid).collection("recipes")
                    .document(selectedRecipe.fireStoreID).collection("photos").document(photo.id)
                    .set(photo)
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }

    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "User Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "User save failed $it") }

        }
    }

    /**
     * Function that provides a list of predictions to the autocomplete text field
     * @param query is text to search
     * @param take  is the number of results to provide
     * @return [List] of [Recipe] containing any predictions
     */
    fun getPredictionList(query: String, take: Int): List<Recipe> {
        var recipeList = recipes.value
        if (query.isEmpty()) return listOf()
        recipeList?.let { list ->
            when {
                list.any { it.name.lowercase().startsWith(query) && it.name != query }
                -> {
                    return list.filter {
                        it.name.lowercase().startsWith(query) && it.name != query
                    }.take(take)
                }
                list.any { it.category.lowercase().startsWith(query) }
                -> {
                    return list.filter {
                        it.category.lowercase().startsWith(query)
                    }.take(take)
                }
                list.any { it.cuisine.lowercase().startsWith(query) }
                -> {
                    return list.filter {
                        it.cuisine.lowercase().startsWith(query)
                    }.take(take)
                }
                else -> return listOf()
            }
        }
        return listOf()
    }
}
