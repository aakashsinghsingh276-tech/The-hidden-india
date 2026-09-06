package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM hidden_places ORDER BY hiddenScore DESC")
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM hidden_places WHERE isSaved = 1 ORDER BY hiddenScore DESC")
    fun getSavedPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM hidden_places WHERE id = :id")
    suspend fun getPlaceById(id: String): PlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)

    @Update
    suspend fun updatePlace(place: PlaceEntity)

    @Query("UPDATE hidden_places SET isSaved = :isSaved WHERE id = :id")
    suspend fun setSavedStatus(id: String, isSaved: Boolean)

    @Query("SELECT COUNT(*) FROM hidden_places")
    suspend fun getCount(): Int
}
