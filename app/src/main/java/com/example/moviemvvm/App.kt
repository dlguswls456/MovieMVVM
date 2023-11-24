package com.example.moviemvvm

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        var instance: App? = null
        fun context(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }
}