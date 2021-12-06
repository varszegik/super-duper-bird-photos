package hu.bme.aut.superbirdphotographer.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.common.api.ApiException
import hu.bme.aut.superbirdphotographer.R


@ExperimentalMaterialApi
@Composable
fun Authentication(
    viewModel: AuthenticationViewModel = viewModel(),
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    viewModel.checkPreviouslySigned(context) {
        navigateToHome()
    }

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    navigateToHome()
                }
            } catch (e: ApiException) {
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Login")
                },
            )
        },
        modifier = Modifier.fillMaxSize()
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginButton {
                authResultLauncher.launch(viewModel.signInRequestCode)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun LoginButton(
    action: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        BirdAnimation()
        var clicked by remember { mutableStateOf(false) }
        Surface(
            onClick = { clicked = !clicked; action() },
            elevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
                    .height(20.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text("Sign in with Google", fontWeight = FontWeight.Medium, color = Color.Gray)
                if (clicked) {
                    Spacer(modifier = Modifier.width(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}


@Composable
fun BirdAnimation() {
    val animationSpec by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bird_feeding))
    val progress by animateLottieCompositionAsState(animationSpec, iterations = Integer.MAX_VALUE)
    LottieAnimation(animationSpec, progress)
}
