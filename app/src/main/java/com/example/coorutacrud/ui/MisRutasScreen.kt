package com.example.coorutacrud.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coorutacrud.data.Ruta
import com.example.coorutacrud.data.RutaDatabase
import com.example.coorutacrud.ui.theme.CooAzulUbicacion
import com.example.coorutacrud.ui.theme.CooRojoDestino
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisRutasScreen() {
    val context = LocalContext.current
    val dao = remember { RutaDatabase.getDatabase(context).rutaDao() }
    val scope = rememberCoroutineScope()

    // READ: la lista se actualiza sola cada vez que cambia la tabla
    val rutas by dao.getAll().collectAsState(initial = emptyList())

    // Estado del formulario
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var asientos by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var rutaEditando by remember { mutableStateOf<Ruta?>(null) }
    var error by remember { mutableStateOf("") }

    fun limpiar() {
        origen = ""; destino = ""; hora = ""; asientos = ""; precio = ""
        rutaEditando = null; error = ""
    }

    fun guardar() {
        val nAsientos = asientos.toIntOrNull()
        val nPrecio = precio.toDoubleOrNull()
        when {
            origen.isBlank() || destino.isBlank() || hora.isBlank() ->
                error = "Completa origen, destino y hora de salida"
            nAsientos == null || nAsientos !in 1..6 ->
                error = "Los asientos deben ser un número del 1 al 6"
            nPrecio == null || nPrecio < 0 ->
                error = "Ingresa un precio válido"
            else -> {
                // Copio los valores ANTES de limpiar el formulario,
                // porque la corrutina se ejecuta después de limpiar()
                val editando = rutaEditando
                val o = origen.trim()
                val d = destino.trim()
                val h = hora.trim()
                scope.launch {
                    if (editando == null) {
                        // CREATE
                        dao.insert(Ruta(origen = o, destino = d,
                            horaSalida = h, asientos = nAsientos, precio = nPrecio))
                    } else {
                        // UPDATE: se conserva el mismo id
                        dao.update(editando.copy(origen = o, destino = d,
                            horaSalida = h, asientos = nAsientos, precio = nPrecio))
                    }
                }
                limpiar()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis rutas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ----- Formulario -----
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            if (rutaEditando == null) "Publicar nueva ruta" else "Editar ruta",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                        Campo(origen, { origen = it }, "Desde (origen)")
                        Campo(destino, { destino = it }, "Hasta (destino)")
                        Campo(hora, { hora = it }, "Hora de salida (ej. 07:00)")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Campo(asientos, { asientos = it }, "Asientos",
                                Modifier.weight(1f), KeyboardType.Number)
                            Campo(precio, { precio = it }, "Precio S/",
                                Modifier.weight(1f), KeyboardType.Decimal)
                        }
                        if (error.isNotEmpty()) {
                            Text(error, color = CooRojoDestino, fontSize = 13.sp)
                        }
                        Button(
                            onClick = { guardar() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (rutaEditando == null) "Publicar ruta" else "Guardar cambios",
                                fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        if (rutaEditando != null) {
                            OutlinedButton(
                                onClick = { limpiar() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text("Cancelar") }
                        }
                    }
                }
            }

            item {
                Text(
                    "Rutas publicadas (${rutas.size})",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (rutas.isEmpty()) {
                item {
                    Text("Todavía no publicaste ninguna ruta.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // ----- Lista (READ) -----
            items(rutas, key = { it.id }) { ruta ->
                TarjetaRuta(
                    ruta = ruta,
                    alEditar = {
                        rutaEditando = ruta
                        origen = ruta.origen
                        destino = ruta.destino
                        hora = ruta.horaSalida
                        asientos = ruta.asientos.toString()
                        precio = ruta.precio.toString()
                        error = ""
                    },
                    // DELETE
                    alEliminar = {
                        scope.launch { dao.delete(ruta) }
                        if (rutaEditando?.id == ruta.id) limpiar()
                    }
                )
            }
        }
    }
}

@Composable
private fun Campo(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = modifier
    )
}

@Composable
private fun TarjetaRuta(ruta: Ruta, alEditar: () -> Unit, alEliminar: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Punto(CooAzulUbicacion, ruta.origen)
            Spacer(Modifier.height(6.dp))
            Punto(CooRojoDestino, ruta.destino)
            Spacer(Modifier.height(10.dp))
            Text(
                "Sale ${ruta.horaSalida}  ·  ${ruta.asientos} asientos  ·  S/ ${"%.2f".format(ruta.precio)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = alEditar) { Text("Editar", color = CooAzulUbicacion) }
                TextButton(onClick = alEliminar) { Text("Eliminar", color = CooRojoDestino) }
            }
        }
    }
}

@Composable
private fun Punto(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).background(color, CircleShape))
        Spacer(Modifier.width(10.dp))
        Text(texto, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
