package com.devx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devx.data.local.dao.AnimeDao
import com.devx.data.local.dao.AnimeDetailDao
import com.devx.data.local.entity.AnimeDetailEntity
import com.devx.data.local.entity.AnimeEntity

@Database(
    entities = [
        AnimeEntity::class,
        AnimeDetailEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun animeDetailDao(): AnimeDetailDao
}
