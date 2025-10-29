package gz.dam.simondize

import androidx.compose.ui.graphics.Color



object Datos {
    var ronda = 0   // <-- nueva variable para la ronda
}

/**
 * Enum con los estados del juego
 *
 */

enum class Estado(val start_activo: Boolean, val boton_activo: Boolean) {
    INICIO(start_activo = true, boton_activo = false),
    GENERANDO(start_activo = false, boton_activo = false),
    ADIVINANDO(start_activo = false, boton_activo = true),
    SIGUIENDO(start_activo = false, boton_activo = false),

    GAMEOVER(start_activo = true, boton_activo = false)

}


/**
 * Colores utilizados
 */

enum class Colores(val color: Color, val txt: String) {
    CLASE_ROJO(color = Color.Red, txt = ""),
    CLASE_VERDE(color = Color.Green, txt = ""),
    CLASE_AZUL(color = Color.Blue, txt = ""),
    CLASE_AMARILLO(color = Color.Yellow, txt = ""),
}


