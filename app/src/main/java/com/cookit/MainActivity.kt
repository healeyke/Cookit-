package com.cookit

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.cookit.dto.Recipe
import com.cookit.ui.theme.CookitTheme

class MainActivity : ComponentActivity() {
    private var inRecipeName: String = ""
    private var selectedRecipe: Recipe? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CookitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecipeFields("Android")
                }
            }
        }
    }

    @Composable
    fun RecipeFields(name: String, recipe: List<Recipe> = ArrayList<Recipe>()) {
        var category by remember { mutableStateOf("") }
        var cuisine by remember { mutableStateOf("") }

        Column {
            SpinnerMenu()
            TextFieldWithDropdownUsage(recipe, label = "Recipe Name")
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
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    //TODO
                }
            )
            {
                Text(text = "Search")
            }
        }
    }

    @Composable
    fun SpinnerMenu () {
        var spinnerText by remember { mutableStateOf("Recipe Collection")}
        var expanded by remember { mutableStateOf(false)}
        val spinnerOptions = listOf(stringResource(R.string.searchRecipe), stringResource(R.string.addRecipe))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier
                .padding(10.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = spinnerText)
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                    DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false }) {
                        spinnerOptions.forEach { selectedOption ->
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    spinnerText = selectedOption
                            }) {
                                Text(text = selectedOption)
                            }
                        }
                    }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Recipe>, label: String = "", take: Int = 3) {
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
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(take)
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
                RecipeFields("Android")
            }
        }
    }
}
