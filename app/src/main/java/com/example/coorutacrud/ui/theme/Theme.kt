package com.example.coorutacrud.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Esquema de Material 3 con los colores de CooRuta (app oscura)
private val EsquemaCooRuta = darkColorScheme(
    primary = CooAzulBoton,
    onPrimary = CooTexto,
    primaryContainer = CooCampoActivo,
    onPrimaryContainer = CooTexto,
    secondary = CooAzulUbicacion,
    onSecondary = CooFondo,
    secondaryContainer = CooPlomo,
    onSecondaryContainer = CooTexto,
    tertiary = CooRojoDestino,
    onTertiary = CooFondo,
    background = CooFondo,
    onBackground = CooTexto,
    surface = CooFondo,
    onSurface = CooTexto,
    surfaceVariant = CooCampo,
    onSurfaceVariant = CooTextoTenue,
    surfaceContainer = CooCampo,
    surfaceContainerHigh = CooCampoActivo,
    surfaceContainerHighest = CooBorde,
    outline = CooBorde,
    error = CooRojoDestino
)

@Composable
fun CooRutaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaCooRuta,
        typography = Typography,
        content = content
    )
}
