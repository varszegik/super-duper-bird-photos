package hu.bme.aut.superbirdphotographer

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.superbirdphotographer.authentication.Authentication
import hu.bme.aut.superbirdphotographer.home.Home
import hu.bme.aut.superbirdphotographer.images.Images
import hu.bme.aut.superbirdphotographer.settings.Settings
import kotlinx.coroutines.launch

object MainDestinations {
  const val HOME_ROUTE = "home"
  const val AUTHENTICATION = "authentication"
  const val IMAGES_ROUTE = "images"
  const val SETTINGS_ROUTE = "settings"
}

@ExperimentalMaterialApi
@Composable
fun BirdPhotographerNavGraph(
  navController: NavHostController = rememberNavController(),
  scaffoldState: ScaffoldState = rememberScaffoldState(),
  startDestination: String = MainDestinations.AUTHENTICATION
) {

  val coroutineScope = rememberCoroutineScope()
  val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

  NavHost(navController = navController, startDestination = startDestination) {
    composable(MainDestinations.AUTHENTICATION) {
      Authentication(navigateToHome = {
        navController.navigate(MainDestinations.HOME_ROUTE) {
          popUpTo(0)
        }
      })
    }
    composable(MainDestinations.HOME_ROUTE) {
      Home(openDrawer = openDrawer)
    }
    composable(MainDestinations.IMAGES_ROUTE) {
      Images(openDrawer = openDrawer)
    }
    composable(MainDestinations.SETTINGS_ROUTE) {
      Settings(openDrawer = openDrawer)
    }
  }
}
