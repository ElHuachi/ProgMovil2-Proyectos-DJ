@file:Suppress("DEPRECATION")

package com.example.login_sicenet.data

import android.util.Log
import com.example.login_sicenet.network.LoginSICEApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx/"

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "text/xml; charset=utf-8")
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    val service: LoginSICEApiService = retrofit.create(LoginSICEApiService::class.java)

}
