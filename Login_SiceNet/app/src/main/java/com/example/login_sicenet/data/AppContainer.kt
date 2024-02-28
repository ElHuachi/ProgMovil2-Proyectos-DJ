package com.example.login_sicenet.data

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.login_sicenet.data.DBRepositories.AccessLoginResponseRepository
import com.example.login_sicenet.data.DBRepositories.AlumnoAcademicoWithLineamientoRepository
import com.example.login_sicenet.data.DBRepositories.CaliFinalesRepository
import com.example.login_sicenet.data.DBRepositories.CaliPorUnidadRepository
import com.example.login_sicenet.data.DBRepositories.CargaAcademicaRepository
import com.example.login_sicenet.data.DBRepositories.KardexItemRepository
import com.example.login_sicenet.data.DBRepositories.OfflineAccessLoginResponseRepository
import com.example.login_sicenet.data.DBRepositories.OfflineAlumnoAcademicoWithLineamientoRepository
import com.example.login_sicenet.data.DBRepositories.OfflineCaliFinalesRepository
import com.example.login_sicenet.data.DBRepositories.OfflineCaliPorUnidadRepository
import com.example.login_sicenet.data.DBRepositories.OfflineCargaAcademicaRepository
import com.example.login_sicenet.data.DBRepositories.OfflineKardexItemRepository
import com.example.login_sicenet.data.DBRepositories.OfflinePromedioRepository
import com.example.login_sicenet.data.DBRepositories.PromedioRepository
import com.example.login_sicenet.network.ReceivedCookiesInterceptor
import com.example.login_sicenet.network.AddCookiesInterceptor
import com.example.login_sicenet.network.DeleteSessionCookies
import com.example.login_sicenet.network.LoginSICEApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import kotlin.coroutines.jvm.internal.*



interface AppContainer {
    val SicenetRepository: SicenetRepository
    val AccessLoginResponseRepository: AccessLoginResponseRepository
    val AlumnoAcademicoWithLineamientoRepository: AlumnoAcademicoWithLineamientoRepository
    val CaliPorUnidadRepository: CaliPorUnidadRepository
    val CaliFinalesRepository: CaliFinalesRepository
    val KardexItemRepository: KardexItemRepository
    val PromedioRepository: PromedioRepository
    val CargaAcademicaRepository: CargaAcademicaRepository
}

class RetrofitClient(context: Context): AppContainer {

    private val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor(context))
        .addInterceptor(ReceivedCookiesInterceptor(context))
        .addInterceptor(createLoggingInterceptor())
        .addInterceptor(DeleteSessionCookies(context))
        .build()

    private fun createLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestHeaders = request.headers
            for (i in 0 until requestHeaders.size) {
                Log.d("HEADER", "${requestHeaders.name(i)}: ${requestHeaders.value(i)}")
            }
            Log.d("Solicitud", "URL: ${request.url}")
            Log.d("Solicitud", "Método: ${request.method}")
            Log.d("Solicitud", "Cuerpo: ${request.body}")
            val response = chain.proceed(request)
            val responseBody = response.body?.string()
            Log.d("Respuesta", "Código: ${response.code}")
            Log.d("Respuesta", "Cuerpo: $responseBody")
            response.newBuilder()
                .body(responseBody?.toResponseBody(response.body?.contentType()))
                .build()
        }
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: LoginSICEApiService by lazy {
        retrofit.create(LoginSICEApiService::class.java)
    }

    override val SicenetRepository: SicenetRepository by lazy {
        NetworkSicenetRepository(retrofitService)
    }

    override val AccessLoginResponseRepository: AccessLoginResponseRepository by lazy {
        OfflineAccessLoginResponseRepository(AlumnoDatabase.getDatabase(context).accessDao())
    }

    override val AlumnoAcademicoWithLineamientoRepository: AlumnoAcademicoWithLineamientoRepository by lazy {
        OfflineAlumnoAcademicoWithLineamientoRepository(AlumnoDatabase.getDatabase(context).profileDao())
    }

    override val CaliPorUnidadRepository: CaliPorUnidadRepository by lazy {
        OfflineCaliPorUnidadRepository(AlumnoDatabase.getDatabase(context).caliPorUnidadDao())
    }

    override val CaliFinalesRepository: CaliFinalesRepository by lazy {
        OfflineCaliFinalesRepository(AlumnoDatabase.getDatabase(context).caliFinalesDao())
    }

    override val KardexItemRepository: KardexItemRepository by lazy {
        OfflineKardexItemRepository(AlumnoDatabase.getDatabase(context).kardexDao())
    }

    override val PromedioRepository: PromedioRepository by lazy {
        OfflinePromedioRepository(AlumnoDatabase.getDatabase(context).promedioDao())
    }

    override val CargaAcademicaRepository: CargaAcademicaRepository by lazy {
        OfflineCargaAcademicaRepository(AlumnoDatabase.getDatabase(context).cargaACDao())
    }
}
