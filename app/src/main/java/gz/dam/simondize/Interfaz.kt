package gz.dam.simondize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Interfaz(miViewModel: MyViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Ronda(miViewModel)
        Spacer(Modifier.size(8.dp))
        Puntuacion(miViewModel)
        Spacer(Modifier.size(10.dp))

        // Fondo detrás de los botones
        Box(
            modifier = Modifier
                .background(color = Color(0xFF020202), shape = RoundedCornerShape(20.dp))
                .padding(10.dp)
        ) {
            Column {
                Row {
                    BotonSimondize(miViewModel, Colores.CLASE_ROJO)
                    Spacer(Modifier.size(16.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_VERDE)
                }

                Spacer(Modifier.size(16.dp))

                Row {
                    BotonSimondize(miViewModel, Colores.CLASE_AZUL)
                    Spacer(Modifier.size(16.dp))
                    BotonSimondize(miViewModel, Colores.CLASE_AMARILLO)
                }
            }
        }

        Spacer(Modifier.size(10.dp))

        Button(onClick = { miViewModel.crearRandom()}) {
            Text("Start")
        }
    }
}


@Composable
fun BotonSimondize(miViewModel: MyViewModel, enum_color: Colores) {
    val activo by miViewModel.botonActivo
    val isActive = activo == enum_color.ordinal

    Button(
        onClick = { miViewModel.comprobar(enum_color.ordinal) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) enum_color.color.copy(alpha = 0.4f , 0.5f) else enum_color.color
        ),
        modifier = Modifier.size(150.dp, 250.dp).padding(3.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(text = enum_color.txt, fontSize = 10.sp)
    }
}
@Composable
fun Puntuacion(miViewModel: MyViewModel, modifier: Modifier = Modifier) {
    val puntos by miViewModel.puntuacion

    Box(
        modifier = modifier
            .fillMaxHeight(0.10f)
            .fillMaxSize(0.9f)
            .background(Color(0x274F84B4))
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Puntos: $puntos",
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}
@Composable
fun Ronda(miViewModel: MyViewModel) {
    val ronda by miViewModel.ronda
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .size(70.dp)
            .background(Color(0xFF14B8CC))
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


