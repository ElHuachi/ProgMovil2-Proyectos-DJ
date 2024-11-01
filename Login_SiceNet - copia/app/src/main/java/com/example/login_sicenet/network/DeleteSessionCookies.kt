package com.example.login_sicenet.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class DeleteSessionCookies(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Borra las cookies en las preferencias compartidas
        context.getSharedPreferences("PREF_COOKIES", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        return chain.proceed(chain.request())
    }
}


