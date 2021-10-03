package hu.bme.aut.superbirdphotographer.ui.screens.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import hu.bme.aut.superbirdphotographer.ui.screens.login.Authentication
import hu.bme.aut.superbirdphotographer.ui.screens.home.Home
import hu.bme.aut.superbirdphotographer.ui.screens.home.HomeViewModel
import hu.bme.aut.superbirdphotographer.ui.screens.images.Images
import hu.bme.aut.superbirdphotographer.ui.screens.settings.Settings
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel

@ExperimentalPermissionsApi
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
      Home(openDrawer = openDrawer, viewModel = hiltViewModel())
    }
    composable(MainDestinations.IMAGES_ROUTE) {
      Images(openDrawer = openDrawer)
    }
    composable(MainDestinations.SETTINGS_ROUTE) {
      Settings(openDrawer = openDrawer)
    }
  }
}
