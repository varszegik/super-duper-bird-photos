package hu.bme.aut.superbirdphotographer

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.superbirdphotographer.home.Home
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val IMAGES_ROUTE = "images"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun BirdPhotographerNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
){
//    val actions = remember(navController) { MainActions(navController) }

    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(navController = navController, startDestination = startDestination){
        composable(MainDestinations.HOME_ROUTE){
            Home(openDrawer = openDrawer)
        }
        composable(MainDestinations.IMAGES_ROUTE){
            Text("This is images")
        }
        composable(MainDestinations.SETTINGS_ROUTE){
            Text("This is settings")
        }
    }
}

class MainActions(navController: NavHostController) {
    val navigateToArticle: (String) -> Unit = { postId: String ->
        navController.navigate("${MainDestinations.SETTINGS_ROUTE}/$postId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}