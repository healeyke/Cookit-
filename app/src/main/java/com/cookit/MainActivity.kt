package com.cookit

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.cookit.dto.Photo
import com.cookit.dto.Recipe
import com.cookit.dto.User
import com.cookit.ui.theme.CookitTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private val viewModel: MainViewModel by viewModel()
    private var inRecipeName: String = ""
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var strUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchRecipes()
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToRecipes()
            }
            viewModel.listenToRecipes()
            val userRecipes by viewModel.userRecipes.observeAsState(initial = emptyList())
            CookitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecipeFields(viewModel.selectedRecipe, userRecipes)
                }
            }
        }
    }

    @Composable
    internal fun RecipeFields(
        selectedRecipe: Recipe = Recipe(),
        userRecipes: List<Recipe> = ArrayList()
    ) {
        val uriHandler = LocalUriHandler.current
        var category by remember(
            key1 = selectedRecipe.recipeID,
            key2 = selectedRecipe.fireStoreID
        ) { mutableStateOf(selectedRecipe.category) }
        var cuisine by remember(
            key1 = selectedRecipe.recipeID,
            key2 = selectedRecipe.fireStoreID
        ) { mutableStateOf(selectedRecipe.cuisine) }
        var ingredients by remember(
            key1 = selectedRecipe.recipeID,
            key2 = selectedRecipe.fireStoreID
        ) { mutableStateOf(viewModel.ingredientMapper.mapToString(selectedRecipe.ingredients)) }
        var instructions by remember(
            key1 = selectedRecipe.recipeID,
            key2 = selectedRecipe.fireStoreID
        ) { mutableStateOf(selectedRecipe.instructions) }
        var youtubeURL by remember(selectedRecipe.recipeID) { mutableStateOf(selectedRecipe.youtubeURL) }

        Column {
            RecipeSpinner(recipes = userRecipes)
            TextFieldWithDropdownUsage(
                label = stringResource(R.string.recipeName),
                selectedRecipe = selectedRecipe
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(text = stringResource(R.string.category)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            OutlinedTextField(
                value = cuisine,
                onValueChange = { cuisine = it },
                label = { Text(text = stringResource(R.string.cuisine)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text(text = stringResource(R.string.ingredients)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(100.dp)
            )
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text(text = stringResource(R.string.instructions)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(100.dp)
            )
            OutlinedTextField(
                value = youtubeURL,
                onValueChange = { youtubeURL = it },
                label = { Text(text = stringResource(R.string.youtubeURL)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            ClickableText(
                modifier = Modifier
                    .padding(10.dp),
                text = linkBuilder(
                    youtubeURL,
                    stringResource(R.string.youtubeLinkText)
                ), onClick = {
                    linkBuilder(youtubeURL).getStringAnnotations("URL", it, it).firstOrNull()
                        ?.let { annotation ->
                            uriHandler.openUri(annotation.item)
                        }
                })
            Row {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        viewModel.selectedRecipe.apply {
                            this.name = inRecipeName
                            this.category = category
                            this.cuisine = cuisine
                            this.instructions = instructions
                            this.ingredients = viewModel.ingredientMapper.stringToMap(ingredients)
                            this.youtubeURL = youtubeURL
                        }
                        viewModel.saveRecipe()
                    }
                )
                {
                    Text(text = stringResource(R.string.Save))
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        signIn()
                    }
                )
                {
                    Text(text = "Logon")
                }

                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        takePhoto()
                    }
                )
                {
                    Text(text = "Photo")
                }
            }
            AsyncImage(model = strUri, contentDescription = "Recipe Image")
        }
    }

    private fun takePhoto() {
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
            invokeCamera()
        } else {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { resultMap ->
            var permissionGranted = false
            resultMap.forEach {
                if (it.value) {
                    permissionGranted = true
                } else {
                    permissionGranted = false
                    return@forEach
                }
            }
            if (permissionGranted) {
                invokeCamera()
            } else {
                Toast.makeText(this, getString(R.string.cameraPermissionsDenied), Toast.LENGTH_LONG)
                    .show()
            }
        }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.cookit.fileprovider", file)
        } catch (e: Exception) {
            Log.e(TAG, "Error:${e.message}") //TAG IMPORT
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss".format(Date()))
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Cookit_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.i(TAG, "Image Location: $uri")
                strUri = uri.toString()
                val photo = Photo(localUri = uri.toString())
                viewModel.photos.add(photo)
            } else {
                Log.e(TAG, "Image not saved. $uri")
            }
        }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Composable
    fun TextFieldWithDropdownUsage(
        label: String = "",
        take: Int = 3,
        selectedRecipe: Recipe = Recipe()
    ) {
        val dropDownOptions = remember { mutableStateOf(listOf<Recipe>()) }
        val textFieldValue =
            remember(selectedRecipe.fireStoreID) { mutableStateOf(TextFieldValue(selectedRecipe.name)) }
        val dropDownExpanded = remember { mutableStateOf(false) }
        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inRecipeName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = viewModel.getPredictionList(value.text, take)
        }
        TextFieldWithDropdown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }

    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Recipe>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                        viewModel.selectedRecipe = text
                    }) {
                        Text(text = text.toString())
                    }
                }
            }
        }
    }

    @Composable
    fun RecipeSpinner(recipes: List<Recipe>) {
        var expanded by remember { mutableStateOf(false) }
        var recipeText by remember { mutableStateOf("Recipe Collection") }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = recipeText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    recipes.forEach { recipe ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            if (recipe.name == viewModel.NEW_RECIPE) {
                                // create a new recipe object
                                recipeText = ""
                            } else {
                                // we have selected an existing recipe
                                recipeText = recipe.toString()
                                inRecipeName = recipe.name
                            }
                            viewModel.selectedRecipe = recipe
                        }) {
                            Text(text = recipe.toString())
                        }
                    }
                }
            }
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signinIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }


    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                viewModel.listenToRecipes()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }

    private fun linkBuilder(
        link: String,
        linkText: String = "Click here to view a YouTube video for this recipe!"
    ): AnnotatedString {
        val output = buildAnnotatedString {
            append(linkText)
            addStringAnnotation(
                tag = "URL",
                annotation = link,
                start = 0,
                end = linkText.length - 1
            )
        }
        return output
    }


    @Preview(name = "Light Mode", showBackground = true)
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )

    @Composable
    fun DefaultPreview() {
        CookitTheme {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                RecipeFields()
            }
        }
    }
}
