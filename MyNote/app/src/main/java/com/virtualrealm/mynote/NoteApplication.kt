package com.virtualrealm.mynote

import android.app.Application
import java.util.TimeZone

class NoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Set default timezone to Jakarta
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"))
    }
}