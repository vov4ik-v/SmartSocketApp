package ai.learning.smartsocketapp.feature.home

import ai.learning.smartsocketapp.R
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val isRelayEnabled by viewModel.isRelayEnabled.collectAsState()
    val isLoading = isRelayEnabled == null


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Crossfade(
            targetState = isRelayEnabled,
            animationSpec = tween(durationMillis = 500), label = ""
        ) { state ->
            Image(
                painter = painterResource(
                    if (isLoading) R.drawable.power_red else (if (state == true) R.drawable.power_green else R.drawable.power_red)
                ),
                contentDescription = "Power button",
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .clickable {
                        viewModel.toggleRelay()
                    }
                    .padding(8.dp)

            )
        }

        Text(
            text = if (isLoading) "Loading..." else (if (isRelayEnabled == true) "Socket Is On" else "Socket Is Off"),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 24.dp)
        )
    }

}