package com.itenary.shared.di

import androidx.room.Room
import com.itenary.shared.data.database.AppDatabase
import com.itenary.shared.data.database.ItineraryDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AndroidDatabaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "itenary.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single<ItineraryDao> { get<AppDatabase>().itineraryDao() }
}
