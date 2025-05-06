package com.virtualrealm.mynote.di

import android.app.Application
import androidx.room.Room
import com.virtualrealm.mynote.NoteDatabase
import com.virtualrealm.mynote.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDatabase): NoteDao {
        return db.noteDao()
    }
}