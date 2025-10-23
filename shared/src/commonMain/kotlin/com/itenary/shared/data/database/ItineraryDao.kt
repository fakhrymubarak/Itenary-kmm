package com.itenary.shared.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItineraryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ItineraryEntity): Long

    @Query("SELECT * FROM itinerary ORDER BY id DESC")
    suspend fun getAll(): List<ItineraryEntity>

    @Query("SELECT * FROM itinerary WHERE id = :id")
    suspend fun getById(id: Long): ItineraryEntity?

    @Delete
    suspend fun delete(entity: ItineraryEntity)
}
