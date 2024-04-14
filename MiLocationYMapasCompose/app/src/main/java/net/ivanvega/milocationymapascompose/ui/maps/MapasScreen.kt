package net.ivanvega.milocationymapascompose.ui.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import com.google.android.gms.maps.GoogleMap
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.ktx.*

@Composable
fun MiMapa(){
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }
}

@Composable
fun MiMapaControlCamara(){
    // Pposición inicial de la cámara
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    // Estado de la camara
    var cameraPosition by remember { mutableStateOf(cameraPositionState.position) }

    // Función para mover la cámara a una nueva posición
    fun moveCameraToPosition(newPosition: LatLng) {
        cameraPosition = CameraPosition.Builder()
            .target(newPosition)
            .zoom(10f)
            .build()
    }

    // Función para mover la cámara con un desplazamiento específico
    fun moveCameraWithOffset(offset: Float) {
        val currentZoom = cameraPosition.zoom
        cameraPosition = CameraPosition.Builder()
            .target(cameraPosition.target)
            .zoom(currentZoom + offset)
            .build()
    }

    LaunchedEffect(cameraPosition) {
        // Recomponer cada vez que cambie la posición de la cámara
        cameraPositionState.position = cameraPosition
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = cameraPosition.target),
            title = "Current Position",
            snippet = "Marker at current position"
        )
    }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { moveCameraToPosition(LatLng(1.35, 103.87)) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Singapore")
        }
        Button(
            onClick = { moveCameraWithOffset(1f) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Zoom In")
        }
        Button(
            onClick = { moveCameraWithOffset(-1f) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Zoom Out")
        }
    }
}

@Composable
fun DrawingMap(){
    var drawnPoints by remember { mutableStateOf(emptyList<LatLng>()) }
    var drawnPolyline by remember { mutableStateOf<List<LatLng>?>(null) }
    var drawnPolygon by remember { mutableStateOf<List<LatLng>?>(null) }
    var drawnCircleCenter by remember { mutableStateOf<LatLng?>(null) }
    var drawnCircleRadius by remember { mutableStateOf<Float?>(null) }
    var isDrawingCircle by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.Builder()
            .target(LatLng(1.35, 103.87))
            .zoom(10f)
            .build()
    }

    fun moveCameraToPosition(newPosition: LatLng) {
        cameraPositionState.position = CameraPosition.Builder()
            .target(newPosition)
            .zoom(10f)
            .build()
    }

    fun moveCameraWithOffset(offset: Float) {
        val currentZoom = cameraPositionState.position.zoom
        cameraPositionState.position = CameraPosition.Builder()
            .target(cameraPositionState.position.target)
            .zoom(currentZoom + offset)
            .build()
    }

    fun startDrawingCircleMode() {
        isDrawingCircle = true
    }

    fun calculateRadius(points: List<LatLng>): Float {
        val firstPoint = points.firstOrNull()
        val lastPoint = points.lastOrNull()

        return if (firstPoint != null && lastPoint != null) {
            val results = FloatArray(1)
            Location.distanceBetween(
                firstPoint.latitude,
                firstPoint.longitude,
                lastPoint.latitude,
                lastPoint.longitude,
                results
            )
            results[0]
        } else {
            0f
        }
    }

    //Limpiar los elementos dibujados
    fun clearDrawnElements() {
        drawnPoints = emptyList()
        drawnPolyline = null
        drawnPolygon = null
        drawnCircleCenter = null
        drawnCircleRadius = null
        isDrawingCircle = false
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            drawnPoints = drawnPoints + latLng
            drawnPolyline = drawnPoints.takeLast(2)
            if (drawnPoints.size >= 3) {
                drawnPolygon = drawnPoints //Dibujar
            }
            if (isDrawingCircle) {
                drawnCircleCenter = latLng //Centro
                drawnCircleRadius = calculateRadius(drawnPoints) //Radio
            }
            drawnCircleCenter = latLng // Dibujar
        }
    ) {
        drawnPolyline?.let { polyline ->
            Polyline(points = polyline, color = Color.Blue, width = 5F)
        }
        drawnPolygon?.let { polygon ->
            Polygon(points = polygon, fillColor = Color.Red.copy(alpha = 0.5f))
        }
        drawnCircleCenter?.let { center ->
            drawnCircleRadius?.let { radius ->
                Circle(center = center, radius = radius.toDouble(), fillColor = Color.Green.copy(alpha = 0.5f))
            }
        }
        drawnPoints.forEach { point ->
            Marker(
                state = MarkerState(position = point)
            )
        }
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .align(Alignment.Top)
        ) {
            Button(
                onClick = { startDrawingCircleMode() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Dibujar Circulo")
            }
            Button(
                onClick = { isDrawingCircle = false },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Modo Circulo Off")
            }
            Button(
                onClick = { clearDrawnElements() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Recomponer")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Top)
        ) {
            Button(
                onClick = { moveCameraToPosition(LatLng(1.35, 103.87)) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Singapore")
            }
            Button(
                onClick = { moveCameraWithOffset(1f) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Zoom In")
            }
            Button(
                onClick = { moveCameraWithOffset(-1f) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Zoom Out")
            }
        }
    }

    drawnPoints.forEach { point ->
        ExtraInfo(
            title = "Coordenadas",
            lat = "Lat: ${point.latitude}",
            lng = "Lng: ${point.longitude}"
        )
    }


}

@Composable
fun ExtraInfo(title: String, lat: String, lng: String) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title)
                Row {
                    Text(text = lat)
                }
                Row {
                    Text(text = lng)
                }
            }
        }
    }
}
