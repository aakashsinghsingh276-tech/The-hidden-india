package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.PlaceEntity
import com.example.data.model.ReviewItem
import com.example.data.model.StoryPost
import com.example.data.remote.NetworkClient
import com.example.data.remote.NominatimResult
import com.example.data.util.ApiConfig
import com.example.data.util.ApiConfigManager
import com.example.data.util.DigipinUtil
import com.example.ui.components.AppDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timeAgo: String,
    val isOnline: Boolean = true,
    val avatarInitial: String
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val placeDao = database.placeDao()
    private val apiConfigManager = ApiConfigManager(application)

    // Current Navigation Destination
    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Selected Place for Detail Screen (null means list view)
    private val _selectedPlace = MutableStateFlow<PlaceEntity?>(null)
    val selectedPlace: StateFlow<PlaceEntity?> = _selectedPlace.asStateFlow()

    // Places from Database
    val allPlaces: StateFlow<List<PlaceEntity>> = placeDao.getAllPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPlaces: StateFlow<List<PlaceEntity>> = placeDao.getSavedPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Category Filter
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Search Query & Nominatim Geocoding results
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<NominatimResult>>(emptyList())
    val searchResults: StateFlow<List<NominatimResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // API Config State
    private val _apiConfig = MutableStateFlow(apiConfigManager.getConfig())
    val apiConfig: StateFlow<ApiConfig> = _apiConfig.asStateFlow()

    // Show API Key Manager Dialog
    private val _showApiDialog = MutableStateFlow(false)
    val showApiDialog: StateFlow<Boolean> = _showApiDialog.asStateFlow()

    // Show Add Place Dialog / Screen
    private val _showAddPlace = MutableStateFlow(false)
    val showAddPlace: StateFlow<Boolean> = _showAddPlace.asStateFlow()

    // Show Chat Dialog / Screen
    private val _showChat = MutableStateFlow(false)
    val showChat: StateFlow<Boolean> = _showChat.asStateFlow()

    // Stories Feed
    val stories = MutableStateFlow(
        listOf(
            StoryPost(
                id = "story_1",
                authorName = "TravelVibes",
                authorHandle = "@travelvibes",
                location = "Tirthan, Himachal Pradesh",
                timeAgo = "2h ago",
                caption = "Such a beautiful hidden place! Tirthan Valley never disappoints ❤️ The crystal river and pine scent are magical.",
                likesCount = 253,
                commentsCount = 32,
                imageDrawableName = "tirthan_valley_1788715016404"
            ),
            StoryPost(
                id = "story_2",
                authorName = "RohitExplores",
                authorHandle = "@rohit_explores",
                location = "Kedarkantha, Uttarakhand",
                timeAgo = "4h ago",
                caption = "Summit push completed at sunrise! Unbelievable snow views of Swargarohini peak 🏔️",
                likesCount = 412,
                commentsCount = 48,
                imageDrawableName = "kedarkantha_trek_1788715045426"
            ),
            StoryPost(
                id = "story_3",
                authorName = "NatureLover",
                authorHandle = "@nature_india",
                location = "Chopta, Uttarakhand",
                timeAgo = "6h ago",
                caption = "Green alpine meadows surrounded by rhododendron blossoms. Mini Switzerland is real!",
                likesCount = 189,
                commentsCount = 19,
                imageDrawableName = "chopta_valley_1788715064959"
            )
        )
    )

    // Chat Travelers
    val chatContacts = MutableStateFlow(
        listOf(
            ChatMessage("1", "Rohit Sharma", "Let's plan a trip together to Tirthan!", "2m", true, "R"),
            ChatMessage("2", "Ananya Verma", "These photos are amazing! What camera?", "12m", true, "A"),
            ChatMessage("3", "TravelVibes", "New reel uploaded from Chopta!", "1h", false, "T"),
            ChatMessage("4", "Aman Singh", "Can you share the exact DIGIPIN location?", "3h", true, "A"),
            ChatMessage("5", "Priya Desai", "Sounds great! Joining next weekend trek.", "5h", false, "P"),
            ChatMessage("6", "MountainLover", "Nice place! Highly recommended for solo travelers.", "1d", false, "M")
        )
    )

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.populateInitialPlaces(placeDao)
            }
        }
    }

    fun setDestination(destination: AppDestination) {
        _currentDestination.value = destination
        _selectedPlace.value = null
    }

    fun selectPlace(place: PlaceEntity?) {
        _selectedPlace.value = place
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.trim().length >= 3) {
            searchNominatim(query.trim())
        } else {
            _searchResults.value = emptyList()
        }
    }

    private fun searchNominatim(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            try {
                val results = withContext(Dispatchers.IO) {
                    NetworkClient.nominatimApi.searchLocations(query = query)
                }
                _searchResults.value = results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun toggleSavePlace(place: PlaceEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val newStatus = !place.isSaved
            placeDao.setSavedStatus(place.id, newStatus)
            if (_selectedPlace.value?.id == place.id) {
                _selectedPlace.value = _selectedPlace.value?.copy(isSaved = newStatus)
            }
        }
    }

    fun saveApiConfig(newConfig: ApiConfig) {
        _apiConfig.value = newConfig
        apiConfigManager.saveConfig(newConfig)
        _showApiDialog.value = false
    }

    fun setApiDialogVisible(visible: Boolean) {
        _showApiDialog.value = visible
    }

    fun setAddPlaceVisible(visible: Boolean) {
        _showAddPlace.value = visible
    }

    fun setChatVisible(visible: Boolean) {
        _showChat.value = visible
    }

    fun addNewPlace(
        name: String,
        description: String,
        category: String,
        state: String,
        latitude: Double,
        longitude: Double,
        bestTime: String,
        safety: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val digipin = DigipinUtil.encode(latitude, longitude)
            val eloc = DigipinUtil.generateMapplseLoc(latitude, longitude)
            val newPlace = PlaceEntity(
                id = "place_${System.currentTimeMillis()}",
                name = name,
                description = description,
                longDescription = "$description Located in $state, offering an offbeat experience away from regular commercial tourism.",
                category = category,
                state = state,
                city = state,
                latitude = latitude,
                longitude = longitude,
                digipin = digipin,
                mapplseLoc = eloc,
                hiddenScore = 85,
                beauty = 90,
                crowd = 80,
                accessibility = 65,
                adventure = 80,
                uniqueness = 85,
                bestTimeToVisit = if (bestTime.isNotBlank()) bestTime else "October – May",
                safetyInfo = if (safety.isNotBlank()) safety else "Moderate. Check local weather.",
                rating = 4.8f,
                reviewsCount = 1,
                savesCount = 0,
                isSaved = false,
                imageDrawableName = "",
                addedBy = "Aakash Singh"
            )
            placeDao.insertPlace(newPlace)
            _showAddPlace.value = false
            _selectedPlace.value = newPlace
        }
    }
}
