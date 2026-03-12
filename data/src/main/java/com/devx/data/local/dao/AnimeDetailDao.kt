package com.devx.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devx.data.local.entity.AnimeDetailEntity

@Dao
interface AnimeDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animeDetail: AnimeDetailEntity)

    @Query("SELECT * FROM anime_detail WHERE malId = :animeId")
    suspend fun getById(animeId: Int): AnimeDetailEntity?
}
