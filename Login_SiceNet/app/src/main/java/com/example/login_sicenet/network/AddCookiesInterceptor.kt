package com.example.login_sicenet.network

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class AddCookiesInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            .getStringSet(PREF_COOKIES, HashSet()) ?: HashSet()
        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
        }
        return chain.proceed(builder.build())
    }

//    fun clearCookies() {
//        PreferenceManager.getDefaultSharedPreferences(context)
//            .edit()
//            .remove(PREF_COOKIES)
//            .apply()
//    }

    fun clearCookies() {
        // Obtiene las cookies actuales
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            .getStringSet(PREF_COOKIES, HashSet()) ?: HashSet()

        // Elimina solo las cookies que contengan "ASP.NET"
        val filteredCookies = preferences.filter { cookie ->
            !cookie.contains("ASP.NET")
        }.toHashSet()

        // Guarda las cookies filtradas en SharedPreferences
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putStringSet(PREF_COOKIES, filteredCookies)
            .apply()
    }

    companion object {
        const val PREF_COOKIES = "PREF_COOKIES"
    }
}
