package com.example.login_sicenet.data

import android.content.Context
import android.util.Log
import com.example.login_sicenet.network.ReceivedCookiesInterceptor
import com.example.login_sicenet.network.AddCookiesInterceptor
import com.example.login_sicenet.network.LoginSICEApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit


class RetrofitClient(context: Context) {

    private val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor(context))
        .addInterceptor(ReceivedCookiesInterceptor(context))
        .addInterceptor(createLoggingInterceptor())
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
            val responseBody = response.body?.string() // Almacenamos el cuerpo de la respuesta en una variable
            Log.d("Respuesta", "Código: ${response.code}")
            Log.d("Respuesta", "Cuerpo: $responseBody") // Usamos la variable en lugar de llamar a response.body?.string() de nuevo
            response.newBuilder()
                .body(responseBody?.toResponseBody(response.body?.contentType()))
                .build()
        }
    }
    
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: LoginSICEApiService by lazy {
        retrofit.create(LoginSICEApiService::class.java)
    }
}
