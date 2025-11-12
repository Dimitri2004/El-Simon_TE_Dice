@file:Suppress("DUPLICATE_BRANCH_CONDITION_IN_WHEN")

package gz.dam.simondize

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Interfaz(miViewModel: MyViewModel) {

    // 🎵 Creamos un ToneGenerator que usaremos para los sonidos

    val botonActivo by miViewModel.botonActivo.observeAsState(-1)
    val error by miViewModel.errorLiveData.observeAsState(false)
    // Sonar automáticamente cuando el ViewModel activa un botón
    LaunchedEffect(botonActivo) {
        when (botonActivo) {
            0 -> miViewModel.reproducirTono(261.63, 150) // DO
            1 -> miViewModel.reproducirTono(293.66, 150) // RE
            2 -> miViewModel.reproducirTono(329.63, 150) // MI
            3 -> miViewModel.reproducirTono(349.23, 150) // FA
        }
        delay(50)
    }
    LaunchedEffect(error) {
        if (error) {
            try {
                // Melodía descendente FA–MI–RE–DO
                val notas = listOf(
                    293.66, // Re
                    329.63, // MI
                    349.23, // FA
                    261.63  // DO
                )
                for (nota in notas) {
                    miViewModel.reproducirTono(nota, 250)
                    delay(200)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Asegura que el flag se resetee incluso si hubo un error
                miViewModel.errorLiveData.value = false
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB9F6CA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Ronda(miViewModel)
        Spacer(Modifier.size(8.dp))
        Puntuacion(miViewModel)
        Spacer(Modifier.size(10.dp))
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column {
                Row {
                    BotonSimondize(miViewModel, Colores.CLASE_ROJO)
                    Spacer(Modifier.size(5.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_VERDE)
                }
                Spacer(Modifier.size(5.dp))
                Row {
                    BotonSimondize(miViewModel, Colores.CLASE_AZUL)
                    Spacer(Modifier.size(5.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_AMARILLO)
                }
            }
        }
        Box {
            Row {
                BotonStart( miViewModel)
            }
        }


    }
}
@Composable
fun BotonSimondize(miViewModel: MyViewModel, enum_color: Colores) {
    val activo by miViewModel.botonActivo.observeAsState(-1)
    val isActive = activo == enum_color.ordinal


    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                // Primero: reproducimos la nota correspondiente al color
                scope.launch {
                    when (enum_color.ordinal) {
                        0 -> miViewModel.reproducirTono(261.63, 100) // DO
                        0 ->delay(150)
                        1 -> miViewModel.reproducirTono(293.66, 100) // RE
                        1 ->delay(150)
                        2 -> miViewModel.reproducirTono(329.63, 100) // MI
                        2 ->delay(150)
                        3 -> miViewModel.reproducirTono(349.23, 100) // FA
                        3 ->delay(150)
                    }
                }

                miViewModel.comprobar(enum_color.ordinal)},
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isActive)
                    enum_color.color.copy(alpha = 0.4f)
                else enum_color.color
            ),
            modifier = Modifier
                .size(150.dp, 250.dp)
                .border(6.dp, Color.Black, RoundedCornerShape(6.dp))
                .padding(3.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = enum_color.txt, fontSize = 0.sp)
        }
    }
}

@Composable
fun BotonStart(miViewModel: MyViewModel) {
    Button(
        onClick = { miViewModel.crearRandom() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF14B8CC)
        ),
        modifier = Modifier
            .size(160.dp, 70.dp)
            .border(6.dp, Color.Black, RoundedCornerShape(6.dp))
            .padding(3.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Start", fontSize = 20.sp)
    }
}
@Composable
fun Puntuacion(model: MyViewModel, modifier: Modifier = Modifier) {
    // Obtener la puntuación del ViewModel despues de haberla cambiado a MutableLiveData
    val puntos by model.puntuacion.observeAsState(0)
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .size(70.dp)
                .border(10.dp, Color.Black, RoundedCornerShape(10.dp))
                .padding(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Puntuación: $puntos",
                fontSize = 30.sp,
                color = Color.Black
            )


    }
}


@Composable
fun Ronda(miViewModel: MyViewModel) {
    val ronda by miViewModel.ronda.observeAsState(0)
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .size(70.dp)
            .border(10.dp, Color.Black, RoundedCornerShape(10.dp))
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ronda: $ronda",
            fontSize = 30.sp,
            color = Color.Black
        )
    }
}



@Preview(showBackground = true)
@Composable
fun IUPreview() {
    Interfaz(MyViewModel())
}



