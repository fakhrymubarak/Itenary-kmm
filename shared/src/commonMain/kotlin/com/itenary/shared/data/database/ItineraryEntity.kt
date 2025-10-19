package com.itenary.shared.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itenary.shared.domain.model.ItineraryDomain

@Entity(tableName = "itinerary")
data class ItineraryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "data")
    val itinerary: ItineraryDomain,
)

