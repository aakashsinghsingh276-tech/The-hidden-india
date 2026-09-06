package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.PlaceEntity
import com.example.data.remote.NetworkClient
import com.example.data.remote.NominatimResult
import com.example.data.util.DigipinUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaceRepository(
    context: Context,
    scope: CoroutineScope
) {
    private val database = AppDatabase.getDatabase(context, scope)
    private val dao = database.placeDao()

    init {
        // Ensure initial seeds are loaded
        scope.launch(Dispatchers.IO) {
            AppDatabase.populateInitialPlaces(dao)
        }
    }

    fun getAllPlaces(): Flow<List<PlaceEntity>> = dao.getAllPlaces()

    fun getSavedPlaces(): Flow<List<PlaceEntity>> = dao.getSavedPlaces()

    suspend fun getPlaceById(id: String): PlaceEntity? = dao.getPlaceById(id)

    suspend fun toggleSave(id: String, currentSaved: Boolean) {
        dao.setSavedStatus(id, !currentSaved)
    }

    suspend fun addNewPlace(place: PlaceEntity) {
        dao.insertPlace(place)
    }

    suspend fun searchLocationsLive(query: String): List<NominatimResult> {
        return try {
            NetworkClient.nominatimApi.searchLocations(query = query)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
