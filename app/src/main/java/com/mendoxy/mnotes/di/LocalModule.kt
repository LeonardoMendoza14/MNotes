package com.mendoxy.mnotes.di

import android.content.Context
import androidx.room.Room
import com.mendoxy.mnotes.data.local.database.MNotesDatabase
import com.mendoxy.mnotes.data.local.database.dao.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MNotesDatabase::class.java,
            "MNotesDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideNotesDao(db: MNotesDatabase): NotesDao = db.notesDao()

}