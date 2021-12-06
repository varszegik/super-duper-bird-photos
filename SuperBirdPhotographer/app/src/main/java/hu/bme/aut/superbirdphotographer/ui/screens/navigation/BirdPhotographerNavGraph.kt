package hu.bme.aut.superbirdphotographer.ui.screens.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import hu.bme.aut.superbirdphotographer.ui.screens.home.Home
import hu.bme.aut.superbirdphotographer.ui.screens.image.ImageScreen
import hu.bme.aut.superbirdphotographer.ui.screens.images.Images
import hu.bme.aut.superbirdphotographer.ui.screens.login.Authentication
import hu.bme.aut.superbirdphotographer.ui.screens.settings.Settings
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
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
            Images(openDrawer = openDrawer, viewModel = hiltViewModel(), navigateToImageScreen = {
                navController.navigate("${MainDestinations.IMAGE_ROUTE}/${it}")
            })
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            Settings(openDrawer = openDrawer)
        }
        composable("${MainDestinations.IMAGE_ROUTE}/{${MainDestinations.IMAGE_URI}}",
            arguments = listOf(navArgument(MainDestinations.IMAGE_URI) {
                type = NavType.StringType
            })
        ) {
            val arg = requireNotNull(it.arguments)
            val imageUri = arg.getString(MainDestinations.IMAGE_URI)
            ImageScreen(imageUri ?: "")
        }
    }
}
