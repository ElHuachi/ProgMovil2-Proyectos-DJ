package net.ivanvega.milocationymapascompose.rutas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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

suspend fun obtenerRutaSafely(origen: String, destino: String): RouteResponse? {
    return try {
        getRuta(origen, destino)
    } catch (e: Exception) {
        // Manejar la excepción de manera adecuada, por ejemplo, registrando el error o notificando al usuario
        Log.e("Obtener Ruta", "Error al obtener la ruta: ${e.message}", e)
        null // Devolver null para indicar que no se pudo obtener la ruta
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val camaraInicio = LatLng(20.126275317533462, -101.18905377998448)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(camaraInicio, 10f)
    }

    var markerDestinoPosition by remember { mutableStateOf<LatLng?>(null) }

    //Almacenar el domicilio del usuario
    LaunchedEffect(key1 = true) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        destino = sharedPreferences.getString("destino", "") ?: ""
        val lat = sharedPreferences.getFloat("destino_lat", 0f).toDouble()
        val lng = sharedPreferences.getFloat("destino_lng", 0f).toDouble()
        if (lat != 0.0 && lng != 0.0) {
            markerDestinoPosition = LatLng(lat, lng)
        }
    }

    Column {
        Button(
            onClick = {
                if(isLocationEnabled(context)){
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
                            cameraPositionState.position = CameraPosition(currentLatLng, 15f,0f,0f)
                        }
                    }
                }else{
                    showToast(activity, "Habilita la ubicación de tu dispositivo")
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
                        if(checkInternetConnection(context)){
                            showToast(activity, "Obteniendo la mejor ruta")
                            ruta = obtenerRutaSafely(origen, destino)
                            if (ruta != null) {
                                showToast(activity, "Ruta encontrada")
                            } else {
                                showToast(activity, "No existe una ruta posible")
                            }
                        }else{
                            showToast(activity, "Revisa tu conexión a internet")
                        }
                    } else {
                        showToast(activity, "Proporcione su ubicación actual y su domicilio")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = "Trazar Ruta")
            Text("Trazar Ruta")
        }

        ruta?.let { route ->
            InfoRuta(route.features.first().properties.summary.distance,route.features.first().properties.summary.duration)
        }
        
        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                if (destino.isEmpty()) {
                    manualDestinationCoordinate = latLng
                    destino = "${latLng.longitude},${latLng.latitude}"
                    markerDestinoPosition = latLng
                    manualDestinationMode = false
                    showToast(activity, "Domicilio establecido")

                    // Guardar la posición del marcador de destino en SharedPreferences
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    sharedPreferences.edit().apply {
                        putString("destino", destino)
                        putFloat("destino_lat", latLng.latitude.toFloat())
                        putFloat("destino_lng", latLng.longitude.toFloat())
                        apply()
                    }

                    ruta = null
                }
            }
        ) {
            markerOrigen?.let {
                Marker(state = MarkerState(position = it), title = "Ubicación actual")
            }

            markerDestinoPosition?.let {
                Marker(state = MarkerState(position = it), title = "Casa")
            }

            ruta?.let { route ->
                val coordenadasRuta =
                    route.features.first().geometry.coordinates.map { LatLng(it[1], it[0]) }
                Polyline(points = coordenadasRuta, color = Color.Red)
            }
        }
    }
}

@Composable
fun InfoRuta(distancia: Double, tiempo: Double) {
    val distanceText = if(distancia<1000){"$distancia mts"}
    else{"%.1f km".format(distancia/1000)}
    val durationMinutesText = when {
        tiempo < 60 -> "%.1f seg (automóvil)".format(tiempo)
        tiempo < 3600 -> "%.1f min (automóvil)".format(tiempo / 60.0)
        tiempo < 86400 -> "%.1f hrs (automóvil)".format(tiempo / 3600.0)
        else -> "%.1f dias (automóvil)".format(tiempo / 86400.0)
    }

    Row(Modifier.background(Color.Transparent)) {
        Icon(
            Icons.Default.Home,
            contentDescription = "Distancia",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(distanceText)
    }

    Row(Modifier.background(Color.Transparent)) {
        Icon(
            Icons.Default.Warning,
            contentDescription = "Duración",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(durationMinutesText)
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

