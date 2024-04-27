package com.daviddj.fcm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    private var _message = ""

    fun setMessage(message: String) {
        _message = message
    }

    fun getMessage(): String {
        return _message
    }

    private var _title = ""

    fun setTitle(title: String) {
        _title = title
    }

    fun getTitle(): String {
        return _title
    }
}