package com.virtualrealm.mynote

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.TimeZone

@HiltAndroidApp
class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Set default timezone to Jakarta
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"))
    }
}