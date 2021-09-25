package hu.bme.aut.superbirdphotographer.authentication

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.superbirdphotographer.R

@ExperimentalMaterialApi
@Composable
fun Authentication(
    navigateToHome: () -> Unit
) {
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
            LoginButton()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun LoginButton() {
    var clicked by remember { mutableStateOf(false) }
    Surface(
        onClick = { clicked = !clicked },
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

@ExperimentalMaterialApi
@Preview
@Composable
fun AuthenticationPreview() {
    Authentication(navigateToHome = {})
}
