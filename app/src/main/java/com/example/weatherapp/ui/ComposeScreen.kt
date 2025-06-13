import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.fetchWeather
import com.example.weatherapp.suggetions.fetchCitySuggestions
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@Composable
fun WeatherScreen() {
    var city by rememberSaveable { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF2196F3)
    val backgroundColor = Color(0xFFE3F2FD)
    val textColor = Color(0xFF0D47A1)

    LaunchedEffect(city) {
        if (city.length >= 2) {
            isLoading = true
            fetchCitySuggestions(city) {
                suggestions = it
                isLoading = false
            }
        } else {
            suggestions = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(backgroundColor, primaryColor.copy(alpha = 0.3f)))
            )
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(16.dp)
    ) {
        Text(
            text = "🌤️ WeatherApp",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City") },
            leadingIcon = {
                Icon(Icons.Default.LocationCity, contentDescription = null)
            },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = primaryColor,
                unfocusedIndicatorColor = primaryColor.copy(alpha = 0.5f),
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                cursorColor = textColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                color = primaryColor
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 150.dp)
                .animateContentSize()
                .padding(vertical = 4.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionCard(suggestion) {
                    city = suggestion
                    suggestions = emptyList()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (city.isNotBlank()) {
                    isLoading = true
                    fetchWeather(city) { weather ->
                        result = weather
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text("Get Weather", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (result.isNotEmpty()) {
            ResultCard(result = result, icon = Icons.Default.Cloud)
        }
    }
}

@Composable
fun SuggestionCard(suggestion: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = suggestion,
            modifier = Modifier.padding(12.dp),
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ResultCard(result: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF42A5F5),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = result,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
