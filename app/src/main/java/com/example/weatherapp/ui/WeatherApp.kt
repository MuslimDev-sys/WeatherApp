import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun WeatherApp() {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen {
            showSplash = false
        }
    } else {
        WeatherScreen()
    }
}
