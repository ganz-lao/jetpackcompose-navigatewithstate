package com.example.naveventtester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.naveventtester.ui.theme.NavEventTesterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavEventTesterTheme {

                val navController = rememberNavController()

                NavGraph(navController = navController)

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph(navController = navController)
                }
            }
        }
    }
}

@Composable
fun ListingScreen(names:List<String>, navController: NavHostController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        names.forEach {
                name -> Text(text = name, color = Color.White, fontSize = 30.sp)
        }
        Button(onClick = {navController.navigate("profile")}) {
            Text(text = "+")
        }
    }
}


@Composable
fun ProfileScreen(
    newName:String,
    navController: NavHostController,
    onNameAdd:() -> Unit,
    onNewNameChange:(String) -> Unit)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = newName,
            onValueChange = onNewNameChange,
            label = {Text(text = "Enter a new name:")}
        )

        Button(onClick = onNameAdd) {
            Text(text = "Add")
        }
        Button(onClick = {navController.navigateUp()}) {
            Text(text = "Back")
        }
    }
}

@Composable
fun NavGraph (navController: NavHostController = rememberNavController()){

    // states will be under NavGraph
    val names = remember { mutableStateListOf<String>() }
    var newName by rememberSaveable { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "listing"
    ) {
        composable(
            route = "listing"
        ) {
            ListingScreen(
                // name list state
                names = names,
                // navController state
                navController = navController
            )
        }
        composable(
            route = "profile"
        ) {
            ProfileScreen(
                // when name add event passed back, add to the list
                onNameAdd = {
                    names.add(newName)
                    // clear the state of new name
                    newName = ""
                },
                // when name change event passed back, update the state
                onNewNameChange = {
                    newName = it
                },
                // navController state
                navController = navController,
                // to be input name state
                newName = newName
            )
        }
    }
}
