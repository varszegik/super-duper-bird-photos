package hu.bme.aut.superbirdphotographer

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.superbirdphotographer.ui.theme.SuperBirdPhotographerTheme
import kotlinx.coroutines.launch


@Composable
fun BirdPhotographerApp() {
    SuperBirdPhotographerTheme {
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        val navController = rememberNavController()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                AppDrawer(
                    navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                    navigateToImages = { navController.navigate(MainDestinations.SETTINGS_ROUTE) },
                    closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                )
            }
        ) {
            BirdPhotographerNavGraph(navController = navController, scaffoldState = scaffoldState)
        }
    }
}

@Composable
@Preview
fun BirdPhotographerAppPreview() {
    BirdPhotographerApp()
}