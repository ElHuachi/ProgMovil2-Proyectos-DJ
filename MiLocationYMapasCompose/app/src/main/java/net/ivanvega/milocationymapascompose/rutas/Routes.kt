package net.ivanvega.milocationymapascompose.rutas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import net.ivanvega.milocationymapascompose.permission.ui.PermissionBox
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class RouteResponse(
    val features: List<RouteFeature>
)

data class RouteFeature(
    val geometry: RouteGeometry,
    val properties: RouteProperties
)

data class RouteGeometry(
    val coordinates: List<List<Double>>
)

data class RouteProperties(
    val summary: RouteSummary
)

data class RouteSummary(
    val distance: Double,
    val duration: Double
)

interface RouteService {
    @GET("/v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): RouteResponse
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openrouteservice.org")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val routeService = retrofit.create(RouteService::class.java)

suspend fun getRuta(origen: String, destino: String): RouteResponse {
    return withContext(Dispatchers.IO) {
        routeService.getRoute(
            apiKey = "5b3ce3597851110001cf6248ca2d3de57ef24174a8577bd2ed97e330",
            start = origen,
            end = destino
        )
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ScreenPrincipal(activity: ComponentActivity) {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            MiMapaRutas(activity)
        },
    )
}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun MiMapaRutas(activity: ComponentActivity) {
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var ruta: RouteResponse? by remember { mutableStateOf(null) }
    var manualDestinationMode by remember { mutableStateOf(false) }
    var manualDestinationCoordinate by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var markerOrigen: LatLng? by remember { mutableStateOf(null) }
    var markerDestino: LatLng? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val camaraInicio = LatLng(20.126275317533462, -101.18905377998448)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(camaraInicio, 10f)
    }

    Column {
        Button(
            onClick = {
                MainScope().launch {
                    showToast(activity, "Obteniendo Ubicación Actual")
                    val priority = Priority.PRIORITY_HIGH_ACCURACY
                    val result = locationClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token,
                    ).await()
                    result?.let { fetchedLocation ->
                        val currentLatLng = LatLng(result.latitude, result.longitude)
                        origen = "${currentLatLng.longitude},${currentLatLng.latitude}"
                        markerOrigen = currentLatLng

                        // Actualizar la posición de la cámara al obtener la ubicación actual
                        cameraPositionState.position = CameraPosition(currentLatLng, 10f,0f,0f)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Ubicación Actual")
            Text("Obtener Ubicación Actual")
        }
        Button(
            onClick = {
                scope.launch {
                    if (origen.isNotEmpty() && destino.isNotEmpty()) {
                        showToast(activity, "Obteniendo la mejor ruta")
                        ruta = getRuta(origen, destino)
                    } else {
                        showToast(activity, "Proporcione su ubicación actual y su destino")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = "Trazar Ruta")
            Text("Trazar Ruta")
        }

        //

        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                manualDestinationCoordinate = latLng
                destino = "${latLng.longitude},${latLng.latitude}"
                markerDestino = latLng
                manualDestinationMode = false
                showToast(activity, "Destino establecido")
                ruta=null
            }
        ) {
            markerOrigen?.let {
                Marker(state = MarkerState(position = it), title = "Ubicación actual")
            }

            markerDestino?.let {
                Marker(state = MarkerState(position = it), title = "Destino")
            }

            ruta?.let { route ->
                val coordenadasRuta =
                    route.features.first().geometry.coordinates.map { LatLng(it[1], it[0]) }
                Polyline(points = coordenadasRuta, color = Color.Red)
            }
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



