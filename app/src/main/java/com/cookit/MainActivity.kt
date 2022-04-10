package com.cookit

import android.content.res.Configuration
import android.os.Bundle
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
import com.cookit.ui.theme.CookitTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private var inRecipeName: String = ""
    private var selectedRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchRecipes()
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
        var category by remember { mutableStateOf("") }
        var cuisine by remember { mutableStateOf("") }
        var ingredients by remember { mutableStateOf("") }
        var instructions by remember { mutableStateOf("") }


        Column {
            RecipeSpinner(recipes = userRecipes)
            TextFieldWithDropdownUsage(label = stringResource(R.string.recipeName))
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
                        var recipe = Recipe().apply {
                            this.name = inRecipeName
                            this.category = category
                            this.cuisine = cuisine
                            this.instructions = instructions
                        }
                        viewModel.save(recipe)
                    }
                )
                {
                    Text(text = stringResource(R.string.Save))
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(label: String = "", take: Int = 3) {
        val dropDownOptions = remember { mutableStateOf(listOf<Recipe>()) }
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
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
                                // create a new specimen object
                                recipeText = ""
                                recipe.name = ""
                            } else {
                                // we have selected an existing specimen.
                                recipeText = recipe.toString()
                                selectedRecipe = Recipe(name = recipe.name)
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
