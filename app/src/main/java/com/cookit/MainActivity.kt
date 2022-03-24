package com.cookit

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cookit.ui.theme.CookitTheme

class MainActivity : ComponentActivity() {
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
}

@Composable
fun RecipeFields(name: String) {
    var recipeName by remember { mutableStateOf("")}
    var category by remember { mutableStateOf("")}
    var cuisine by remember { mutableStateOf("")}
    
    Column {

        // dropdown for add vs. edit recipes

        OutlinedTextField(
            value = recipeName,
            onValueChange = {recipeName = it},
            label = { Text(stringResource(R.string.recipeName))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        OutlinedTextField(
            value = category,
            onValueChange = {category = it},
            label = { Text(text = stringResource(R.string.category))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        OutlinedTextField(
            value = cuisine,
            onValueChange = {cuisine = it},
            label = { Text(text = stringResource(R.string.cuisine))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        Button (
            modifier = Modifier
                .padding(10.dp),
            onClick = { /*TODO*/ }
        )
        {
            Text(text = "Search")
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