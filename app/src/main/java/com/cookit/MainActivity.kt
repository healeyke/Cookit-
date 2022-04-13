package com.cookit

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.cookit.dto.Recipe
import com.cookit.dto.User
import com.cookit.ui.theme.CookitTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private var inRecipeName: String = ""
    private var selectedRecipe: Recipe? = null
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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
            val apiRecipes by viewModel.recipes.observeAsState(initial = emptyList())
            val userRecipes by viewModel.userRecipes.observeAsState(initial = emptyList())
            CookitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecipeFields(apiRecipes, viewModel.selectedRecipe, userRecipes)
                }
            }
        }
    }

    @Composable
    internal fun RecipeFields(
        apiRecipes: List<Recipe> = ArrayList(),
        selectedRecipe: Recipe = Recipe(),
        userRecipes: List<Recipe> = ArrayList()
    ) {
        var category by remember (selectedRecipe.fireStoreID) { mutableStateOf(selectedRecipe.category) }
        var cuisine by remember (selectedRecipe.fireStoreID) { mutableStateOf(selectedRecipe.cuisine) }
        var ingredients by remember { mutableStateOf("") }
        var instructions by remember (selectedRecipe.fireStoreID) { mutableStateOf(selectedRecipe.instructions) }

        Column {
            RecipeSpinner(recipes = userRecipes)
            TextFieldWithDropdownUsage(label = stringResource(R.string.recipeName), selectedRecipe = selectedRecipe)
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
                label = { Text(text = stringResource(R.string.istructions)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(100.dp)
            )
            Row {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        //TODO
                    }
                )
                {
                    Text(text = stringResource(R.string.Search))
                }
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
                        }
                        viewModel.saveRecipe()
                    }
                )
                {
                    Text(text = stringResource(R.string.Save))
                }
                Button (
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        signIn()
                    }
                )
                {
                    Text(text = "Logon")
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(label: String = "", take: Int = 3, selectedRecipe: Recipe = Recipe()) {
        val dropDownOptions = remember { mutableStateOf(listOf<Recipe>()) }
        val textFieldValue = remember(selectedRecipe.fireStoreID) { mutableStateOf(TextFieldValue(selectedRecipe.name)) }
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
                        selectedRecipe = text
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
    private val signInLauncher = registerForActivityResult (
        FirebaseAuthUIActivityResultContract()
    ) {
            res -> this.signInResult(res)
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
