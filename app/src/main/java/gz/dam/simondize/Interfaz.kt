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
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.*


@Composable
fun Interfaz(miViewModel: MyViewModel) {

    // 🎵 Creamos un ToneGenerator que usaremos para los sonidos
    val toneGen = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    val botonActivo by miViewModel.botonActivo.observeAsState(-1)
    val error by miViewModel.errorLiveData.observeAsState(false)
    // 🔔 Sonar automáticamente cuando el ViewModel activa un botón
    LaunchedEffect(botonActivo) {
        when (botonActivo) {
            0 -> toneGen.startTone(ToneGenerator.TONE_DTMF_1, 200) // DO
            1 -> toneGen.startTone(ToneGenerator.TONE_DTMF_2, 200) // RE
            2 -> toneGen.startTone(ToneGenerator.TONE_DTMF_3, 200) // MI
            3 -> toneGen.startTone(ToneGenerator.TONE_DTMF_4, 200) // FA
        }
    }
    LaunchedEffect(error) {
        if (error) {
            // Melodía descendente FA–MI–RE–DO
            val notas = listOf(
                ToneGenerator.TONE_DTMF_4,
                ToneGenerator.TONE_DTMF_3,
                ToneGenerator.TONE_DTMF_2,
                ToneGenerator.TONE_DTMF_1
            )
            for (nota in notas) {
                toneGen.startTone(nota, 150)
                kotlinx.coroutines.delay(240)
            }
            // Reseteamos el flag
            miViewModel.errorLiveData.value = false
        }
    }

    // 🧹 Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose { toneGen.release() }
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
                    BotonSimondize(miViewModel, Colores.CLASE_ROJO,toneGen)
                    Spacer(Modifier.size(5.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_VERDE,toneGen)
                }
                Spacer(Modifier.size(5.dp))
                Row {
                    BotonSimondize(miViewModel, Colores.CLASE_AZUL,toneGen)
                    Spacer(Modifier.size(5.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_AMARILLO,toneGen)
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
fun BotonSimondize(miViewModel: MyViewModel, enum_color: Colores, toneGen: ToneGenerator) {
    val activo by miViewModel.botonActivo.observeAsState(-1)
    val isActive = activo == enum_color.ordinal
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                    // 🔊 Sonido manual al pulsar
                    when (enum_color.ordinal) {
                        0 -> toneGen.startTone(ToneGenerator.TONE_DTMF_1, 200)
                        1 -> toneGen.startTone(ToneGenerator.TONE_DTMF_2, 200)
                        2 -> toneGen.startTone(ToneGenerator.TONE_DTMF_3, 200)
                        3 -> toneGen.startTone(ToneGenerator.TONE_DTMF_4, 200)
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



