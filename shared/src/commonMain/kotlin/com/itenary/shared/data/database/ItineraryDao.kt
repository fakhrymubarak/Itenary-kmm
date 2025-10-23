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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ItineraryEntity): Long

    @Query("SELECT * FROM itinerary ORDER BY id DESC")
    suspend fun getAll(): List<ItineraryEntity>

    @Query("SELECT * FROM itinerary WHERE id = :id")
    suspend fun getByKey(id: String): ItineraryEntity?

    @Delete
    suspend fun delete(entity: ItineraryEntity)

    @Query("SELECT id FROM itinerary")
    suspend fun getAllIds(): List<String>
}
