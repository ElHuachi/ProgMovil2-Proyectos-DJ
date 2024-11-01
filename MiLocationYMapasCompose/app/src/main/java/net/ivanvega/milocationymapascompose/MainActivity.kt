package net.ivanvega.milocationymapascompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import net.ivanvega.milocationymapascompose.rutas.ScreenPrincipal
import net.ivanvega.milocationymapascompose.ui.location.CurrentLocationScreen
import net.ivanvega.milocationymapascompose.ui.maps.DrawingMap
import net.ivanvega.milocationymapascompose.ui.maps.MiMapa
import net.ivanvega.milocationymapascompose.ui.maps.MiMapaControlCamara
import net.ivanvega.milocationymapascompose.ui.maps.StreetView
import net.ivanvega.milocationymapascompose.ui.theme.MiLocationYMapasComposeTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiLocationYMapasComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //CurrentLocationScreen()
                    //MiMapaControlCamara()
                    //DrawingMap()
                    //StreetView()
                    ScreenPrincipal(this)
                }
            }
        }
    }
}
