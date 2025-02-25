package com.virtualrealm.Kuliah.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val username: String,
    val password: String,
    val nama: String,
    val nomorTelepon: String,
    val email: String,
    val alamat: String
)