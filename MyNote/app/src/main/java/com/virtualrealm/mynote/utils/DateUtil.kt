package com.virtualrealm.mynote.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {
    // Tidak perlu anotasi @RequiresApi karena kita menggunakan desugaring
    private val jakartaZone = ZoneId.of("Asia/Jakarta")
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")

    fun getCurrentJakartaDateTime(): String {
        val current = LocalDateTime.now(jakartaZone)
        return current.format(formatter)
    }

    fun formatDateTime(dateTime: String): String {
        return try {
            val parsed = LocalDateTime.parse(dateTime, formatter)
            parsed.format(formatter)
        } catch (e: Exception) {
            dateTime // Return original jika parsing gagal
        }
    }
}