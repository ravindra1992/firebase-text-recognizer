package com.firebase.textrecognizer.ui.activity

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class App : Application() {
    init {
        instance = this
    }
    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        val context: Context = App.applicationContext()
        FirebaseApp.initializeApp(this)
    }
}