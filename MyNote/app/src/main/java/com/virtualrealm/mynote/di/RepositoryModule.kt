package com.virtualrealm.mynote.di

import com.virtualrealm.mynote.dao.NoteDao
import com.virtualrealm.mynote.networks.NoteApi
import com.virtualrealm.mynote.repositories.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideNoteRepository(
        api: NoteApi,
        dao: NoteDao
    ): NoteRepository {
        return NoteRepository(api, dao)

    }
}