package gz.dam.simondize

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {
    private val TAG_LOG = "miDebug"

    // Estado del juego
    val estadoLiveData = mutableStateOf(Estado.INICIO)

    // Secuencia de colores (0=Rojo,1=Verde,2=Azul,3=Amarillo)
    private val secuencia = mutableListOf<Int>()
    private var indiceJugador = 0

    // Puntuación
    val puntuacion = mutableStateOf(0)

    // Botón activo para animación
    val botonActivo = mutableStateOf(-1)

    val ronda = mutableStateOf(1)

    private val scope = CoroutineScope(Dispatchers.Main)

    // Genera un nuevo color y muestra la secuencia
    fun crearRandom() {
        if (estadoLiveData.value != Estado.INICIO) return

        estadoLiveData.value = Estado.GENERANDO

        // Generar color aleatorio y agregar a la secuencia
        val nuevo = (0..3).random()
        secuencia.add(nuevo)
        indiceJugador = 0
        mostrarSecuencia()
    }

    private fun mostrarSecuencia() {
        estadoLiveData.value = Estado.GENERANDO

        scope.launch {
            for (color in secuencia) {
                botonActivo.value = color
                delay(500)
                botonActivo.value = -1
                delay(200)
            }
            estadoLiveData.value = Estado.ADIVINANDO
        }
    }

    fun comprobar(ordinal: Int) {
        if (estadoLiveData.value != Estado.ADIVINANDO) return

        if (secuencia[indiceJugador] == ordinal) {
            indiceJugador++
            if (indiceJugador == secuencia.size) {
                // Secuencia completa correcta
                puntuacion.value++
                // Generar siguiente ronda automáticamente
                generarSiguienteRonda()
            }
        } else {
            // Fallo
            secuencia.clear()
            indiceJugador = 0
            puntuacion.value = 0
            estadoLiveData.value = Estado.INICIO
        }
    }
    fun generarSiguienteRonda() {
        val nuevo = (0..3).random()
        secuencia.add(nuevo)
        indiceJugador = 0       // reiniciamos el índice del jugador
        ronda.value = secuencia.size
        mostrarSecuencia()      // mostramos toda la secuencia actualizada
    }
}