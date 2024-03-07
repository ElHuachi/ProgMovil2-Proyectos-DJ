package com.example.login_sicenet

import android.app.Application
import androidx.work.WorkManager
import com.example.login_sicenet.data.AppContainer
import com.example.login_sicenet.data.RetrofitClient

class SicenetApplication: Application() {
    lateinit var container: AppContainer
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        container = RetrofitClient(this)
        workManager = WorkManager.getInstance(this)
    }
}