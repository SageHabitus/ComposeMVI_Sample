package com.example.composemvi.core

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposeMVIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(CustomLogAdapter())
    }
}

class CustomLogAdapter : AndroidLogAdapter() {
    override fun log(priority: Int, tag: String?, message: String) {
        val customTag =
            tag ?: run {
                val stackTrace = Throwable().stackTrace
                val className = stackTrace[6].className.substringAfterLast('.')
                className
            }

        super.log(priority, customTag, message)
    }
}
