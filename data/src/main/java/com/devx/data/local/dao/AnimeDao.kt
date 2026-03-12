package com.devx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devx.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM anime ORDER BY id ASC")
    fun observeAll(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime ORDER BY id ASC")
    suspend fun getAll(): List<AnimeEntity>

    @Query("DELETE FROM anime")
    suspend fun clearAll()
}
