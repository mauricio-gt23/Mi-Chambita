package com.michambita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michambita.ui.theme.MiChambitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MiChambitaTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Mi Chambita",
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bienvenido a $name 👋",
            style = typography.titleLarge,
            color = colors.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = cardColors(containerColor = colors.surface),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Esta tarjeta muestra el color `surface` y `onSurface`.",
                style = typography.bodyLarge,
                color = colors.onSurface,
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Text(text = "Botón de prueba")
        }

        Text(
            text = if (isSystemInDarkTheme()) "🌙 Tema oscuro activado" else "☀️ Tema claro activado",
            style = typography.bodyLarge,
            color = colors.secondary
        )

        Divider()

        Text(
            text = "🎨 Paleta de colores",
            style = typography.titleLarge
        )

        ColorSample("Primary", colors.primary, colors.onPrimary)
        ColorSample("Secondary", colors.secondary, colors.onSecondary)
        ColorSample("Background", colors.background, colors.onBackground)
        ColorSample("Surface", colors.surface, colors.onSurface)
        ColorSample("Error", colors.error, Color.White)
        ColorSample("Outline", colors.outline, colors.onSurface)
    }
}

@Composable
fun ColorSample(label: String, color: Color, contentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = cardColors(containerColor = color),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = label, color = contentColor)
        }
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun GreetingPreviewLight() {
    MiChambitaTheme(darkTheme = false) {
        Greeting("MiChambita")
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun GreetingPreviewDark() {
    MiChambitaTheme(darkTheme = true) {
        Greeting("MiChambita")
    }
}