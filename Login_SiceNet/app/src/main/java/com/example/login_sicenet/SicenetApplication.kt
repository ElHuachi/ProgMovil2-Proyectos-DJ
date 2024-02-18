package com.example.login_sicenet

import android.app.Application
import com.example.login_sicenet.data.AppContainer
import com.example.login_sicenet.data.RetrofitClient

class SicenetApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = RetrofitClient(this)
    }
}