package dev.ricknout.composesensors.demo.ui.accelerometer

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import dev.ricknout.composesensors.demo.model.Demo
import dev.ricknout.composesensors.demo.ui.Demo
import dev.ricknout.composesensors.demo.ui.NotAvailableDemo

@Composable
fun AccelerometerDemo() {
    if (isAccelerometerSensorAvailable()) {
        val sensorValue by rememberAccelerometerSensorValueAsState()
        val (x, y, z) = sensorValue.value
        Demo(
            demo = Demo.ACCELEROMETER,
            value = "X: $x m/s^2\nY: $y m/s^2\nZ: $z m/s^2",
        ) {
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()
            var center by remember { mutableStateOf(Offset(width / 2, height / 2)) }
            val orientation = LocalConfiguration.current.orientation
            val contentColor = LocalContentColor.current
            val radius = with(LocalDensity.current) { 10.dp.toPx() }
            center = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Offset(
                    x = (center.x - x).coerceIn(radius, width - radius),
                    y = (center.y + y).coerceIn(radius, height - radius),
                )
            } else {
                Offset(
                    x = (center.x + y).coerceIn(radius, width - radius),
                    y = (center.y + x).coerceIn(radius, height - radius),
                )
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = contentColor,
                    radius = radius,
                    center = center,
                )
            }
        }
    } else {
        NotAvailableDemo(demo = Demo.ACCELEROMETER)
    }
}

@Composable
fun CanchaFP() {
    if (isAccelerometerSensorAvailable()) {
        val sensorValue by rememberAccelerometerSensorValueAsState()
        val (x, y, z) = sensorValue.value

        // Bordes de la cancha
        val leftLimit = 60.dp
        val rightLimit = 1020.dp
        val topLimit = 60.dp
        val bottomLimit = 1850.dp

        val porteriaHeight = 50.dp
        val porteriaWidth = 150.dp

        // Zona de anotación
        val topGoalTopLimit = topLimit
        val topGoalBottomLimit = topLimit + porteriaHeight
        val bottomGoalTopLimit = bottomLimit - porteriaHeight
        val bottomGoalBottomLimit = bottomLimit

        val porteriaLeftLimit = (rightLimit.value - porteriaWidth.value) / 2
        val porteriaRightLimit = porteriaLeftLimit + porteriaWidth.value

        var score by remember { mutableStateOf(0) }

        val obstaculos = listOf(
            //HORIZONTALES PORTERIA SUPERIR
            Obstaculo(30.dp, 50.dp, 40.dp, 10.dp),
            Obstaculo(90.dp, 50.dp, 40.dp, 10.dp),
            Obstaculo(180.dp, 50.dp, 40.dp, 10.dp),
            Obstaculo(260.dp, 50.dp, 40.dp, 10.dp),
            Obstaculo(320.dp, 50.dp, 20.dp, 10.dp),

            //VERTICALES PORTERIA SUPERIOR
            Obstaculo(40.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(60.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(100.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(140.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(180.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(220.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(280.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(300.dp, 70.dp, 10.dp, 20.dp),
            Obstaculo(350.dp, 70.dp, 10.dp, 20.dp),

            //HORIZONTALES INFERIORES
            Obstaculo(50.dp, 250.dp, 40.dp, 10.dp),
            Obstaculo(110.dp, 250.dp, 40.dp, 10.dp),
            Obstaculo(170.dp, 250.dp, 40.dp, 10.dp),
            Obstaculo(240.dp, 250.dp, 40.dp, 10.dp),
            Obstaculo(310.dp, 250.dp, 20.dp, 10.dp),

            //VERTICALES SUPERIRORES
            Obstaculo(50.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(100.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(150.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(200.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(250.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(300.dp, 100.dp, 10.dp, 80.dp),
            Obstaculo(330.dp, 100.dp, 10.dp, 80.dp),

            //VERTICALES SUPERIRORES
            Obstaculo(40.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(60.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(100.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(140.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(180.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(220.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(280.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(300.dp, 200.dp, 10.dp, 20.dp),
            Obstaculo(350.dp, 200.dp, 10.dp, 20.dp),

            //HORIZONTALES CENTRO
            Obstaculo(30.dp, 300.dp, 40.dp, 10.dp),
            Obstaculo(90.dp, 300.dp, 40.dp, 10.dp),
            Obstaculo(180.dp, 300.dp, 40.dp, 10.dp),
            Obstaculo(260.dp, 300.dp, 40.dp, 10.dp),
            Obstaculo(320.dp, 300.dp, 20.dp, 10.dp),

            Obstaculo(30.dp, 350.dp, 40.dp, 10.dp),
            Obstaculo(90.dp, 350.dp, 40.dp, 10.dp),
            Obstaculo(180.dp, 350.dp, 40.dp, 10.dp),
            Obstaculo(260.dp, 350.dp, 40.dp, 10.dp),
            Obstaculo(320.dp, 350.dp, 20.dp, 10.dp),

            //VERTICALES INFERIORES
            Obstaculo(50.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(100.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(150.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(200.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(250.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(300.dp, 400.dp, 10.dp, 80.dp),
            Obstaculo(330.dp, 400.dp, 10.dp, 80.dp),

            //HORIZONTALES INFERIORES
            Obstaculo(30.dp, 500.dp, 40.dp, 10.dp),
            Obstaculo(90.dp, 500.dp, 40.dp, 10.dp),
            Obstaculo(180.dp, 500.dp, 40.dp, 10.dp),
            Obstaculo(260.dp, 500.dp, 40.dp, 10.dp),
            Obstaculo(320.dp, 500.dp, 20.dp, 10.dp),

            //HORIZONTALES INFERIORES
            Obstaculo(50.dp, 550.dp, 40.dp, 10.dp),
            Obstaculo(110.dp, 550.dp, 40.dp, 10.dp),
            Obstaculo(170.dp, 550.dp, 40.dp, 10.dp),
            Obstaculo(240.dp, 550.dp, 40.dp, 10.dp),
            Obstaculo(310.dp, 550.dp, 20.dp, 10.dp),

            //VERTICALES PORTERIA INFERIOR
            Obstaculo(40.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(60.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(100.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(140.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(180.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(220.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(280.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(300.dp, 600.dp, 10.dp, 20.dp),
            Obstaculo(350.dp, 600.dp, 10.dp, 20.dp),

            //HORIZONTALES PORTERIA INFERIOR
            Obstaculo(30.dp, 630.dp, 40.dp, 10.dp),
            Obstaculo(90.dp, 630.dp, 40.dp, 10.dp),
            Obstaculo(180.dp, 630.dp, 40.dp, 10.dp),
            Obstaculo(260.dp, 630.dp, 40.dp, 10.dp),
            Obstaculo(320.dp, 630.dp, 20.dp, 10.dp),
        )


        Demo(
            demo = Demo.ACCELEROMETER,
            value = "X: $x m/s^2\nY: $y m/s^2\nZ: $z m/s^2",
        ) {
            val orientation = LocalConfiguration.current.orientation
            val contentColor = LocalContentColor.current
            val radius = with(LocalDensity.current) { 10.dp.toPx() }

            var center by remember {
                mutableStateOf(
                    Offset(
                        x = (leftLimit.value + rightLimit.value) / 2,
                        y = (topLimit.value + bottomLimit.value) / 2 // Start at the center of the field
                    )
                )
            }

            center = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Offset(
                    x = (center.x - x*2).coerceIn(leftLimit.value + radius, rightLimit.value - radius),
                    y = (center.y + y*2).coerceIn(topLimit.value + radius, bottomLimit.value - radius),
                )
            } else {
                Offset(
                    x = (center.x + y*2).coerceIn(leftLimit.value + radius, rightLimit.value - radius),
                    y = (center.y + x*2).coerceIn(topLimit.value + radius, bottomLimit.value - radius),
                )
            }

            // Check for collisions with obstacles
            obstaculos.forEach { obstacle ->
                center = checkCollision(center, obstacle, radius)
            }

            // Conteo de goles
            if ((center.y - radius <= topGoalBottomLimit.value &&
                        center.y + radius >= topGoalTopLimit.value &&
                        center.x - radius >= porteriaLeftLimit &&
                        center.x + radius <= porteriaRightLimit) ||
                (center.y + radius >= bottomGoalTopLimit.value &&
                        center.y - radius <= bottomGoalBottomLimit.value &&
                        center.x - radius >= porteriaLeftLimit &&
                        center.x + radius <= porteriaRightLimit)
            ) {
                score++
                center = Offset(
                    x = (leftLimit.value + rightLimit.value) / 2,
                    y = (topLimit.value + bottomLimit.value) / 2
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mostrar y actualizar puntaje
                Text(
                    text = "Puntaje: $score",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                // Dibujar cancha
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Dibujar Porterías
                    Box(
                        modifier = Modifier
                            .width(porteriaWidth)
                            .height(porteriaHeight)
                            .align(Alignment.TopCenter)
                            .background(Color.Transparent)
                            .border(4.dp, Color.White)
                    )
                    Box(
                        modifier = Modifier
                            .width(porteriaWidth)
                            .height(porteriaHeight)
                            .align(Alignment.BottomCenter)
                            .background(Color.Transparent)
                            .border(4.dp, Color.White)
                    )

                    // Campo de juego
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .background(Color.Transparent)
                            .border(4.dp, Color.White)
                    ) {
                        // Línea central
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .height(4.dp),
                            color = Color.White
                        )

                        // Circulo y punto central
                        CentroCancha()
                    }

                    // Dibujar obstáculos
                    obstaculos.forEach { obstaculo ->
                        Box(
                            modifier = Modifier
                                .offset(obstaculo.left, obstaculo.top)
                                .size(obstaculo.width, obstaculo.height)
                                .background(Color.DarkGray)
                        )
                    }

                    // Dibujar la pelota
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White,
                            radius = radius,
                            center = center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CentroCancha() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val canchaWidth = maxWidth
        val canchaHeight = maxHeight
        val radioCentro = 100.dp
        val radioPunto = 5.dp

        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerField = Offset(canchaWidth.toPx() / 2, canchaHeight.toPx() / 2)

            // Círculo central
            drawCircle(
                color = Color.White,
                radius = radioCentro.toPx(),
                center = centerField,
                style = Stroke(width = 4.dp.toPx())
            )

            // Punto central
            drawCircle(
                color = Color.White,
                radius = radioPunto.toPx(),
                center = centerField
            )
        }
    }
}

data class Obstaculo(val left: Dp, val top: Dp, val width: Dp, val height: Dp)

@Composable
fun checkCollision(centro: Offset, obstaculo: Obstaculo, radio: Float): Offset {
    val obstacleRect = with(LocalDensity.current) {
        androidx.compose.ui.geometry.Rect(
            left = obstaculo.left.toPx(),
            top = obstaculo.top.toPx(),
            right = obstaculo.left.toPx() + obstaculo.width.toPx(),
            bottom = obstaculo.top.toPx() + obstaculo.height.toPx()
        )
    }

    // Verificar colision
    val cercanoX = centro.x.coerceIn(obstacleRect.left, obstacleRect.right)
    val cercanoY = centro.y.coerceIn(obstacleRect.top, obstacleRect.bottom)
    val distanciaX = centro.x - cercanoX
    val distanciaY = centro.y - cercanoY

    if ((distanciaX * distanciaX + distanciaY * distanciaY) < (radio * radio)) {
        //Colisión detectada, calcular nueva posición después del rebote
        val penetrationDepthX = radio - kotlin.math.abs(distanciaX)
        val penetrationDepthY = radio - kotlin.math.abs(distanciaY)

        return when {
            kotlin.math.abs(distanciaX) > kotlin.math.abs(distanciaY) -> {
                // Rebote horizontal
                Offset(centro.x + kotlin.math.sign(distanciaX) * penetrationDepthX, centro.y)
            }
            else -> {
                //Rebote vertical
                Offset(centro.x, centro.y + kotlin.math.sign(distanciaY) * penetrationDepthY)
            }
        }
    }
    return centro
}

