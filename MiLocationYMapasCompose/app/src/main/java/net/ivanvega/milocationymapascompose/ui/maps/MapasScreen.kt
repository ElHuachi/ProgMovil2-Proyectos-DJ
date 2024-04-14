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
import com.google.maps.android.compose.Circle
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

