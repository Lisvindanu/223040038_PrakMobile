package com.virtualrealm.Kuliah.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?


    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getFirstUser(): User?

    // Add this method for updating user profile
    @Query("UPDATE user_table SET nama = :nama, nomorTelepon = :nomorTelepon, email = :email, alamat = :alamat WHERE username = :username")
    suspend fun updateUserProfile(username: String, nama: String, nomorTelepon: String, email: String, alamat: String)

    // Alternatively, you can use this method that takes a User object
    @Update
    suspend fun updateUserProfile(user: User)
}