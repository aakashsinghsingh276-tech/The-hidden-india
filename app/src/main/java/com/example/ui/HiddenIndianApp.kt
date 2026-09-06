package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppDestination
import com.example.ui.components.AppHeader
import com.example.ui.screens.*
import com.example.ui.theme.DarkBackground
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HiddenIndianApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val selectedPlace by viewModel.selectedPlace.collectAsState()
    val allPlaces by viewModel.allPlaces.collectAsState()
    val savedPlaces by viewModel.savedPlaces.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val chatContacts by viewModel.chatContacts.collectAsState()
    val apiConfig by viewModel.apiConfig.collectAsState()
    val showApiDialog by viewModel.showApiDialog.collectAsState()
    val showAddPlace by viewModel.showAddPlace.collectAsState()
    val showChat by viewModel.showChat.collectAsState()

    // Handle back button when viewing a place detail
    BackHandler(enabled = selectedPlace != null) {
        viewModel.selectPlace(null)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        containerColor = DarkBackground,
        topBar = {
            // Only show main app header when NOT in Place Detail or full Reel view
            if (selectedPlace == null && currentDestination != AppDestination.REELS) {
                AppHeader(
                    onSearchClick = {
                        viewModel.setDestination(AppDestination.EXPLORE)
                    },
                    onApiSettingsClick = {
                        viewModel.setApiDialogVisible(true)
                    },
                    onChatClick = {
                        viewModel.setChatVisible(true)
                    },
                    onAddPlaceClick = {
                        viewModel.setAddPlaceVisible(true)
                    }
                )
            }
        },
        bottomBar = {
            if (selectedPlace == null) {
                AppBottomNav(
                    currentDestination = currentDestination,
                    onNavigate = { dest ->
                        viewModel.setDestination(dest)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedPlace != null) {
                PlaceDetailScreen(
                    place = selectedPlace!!,
                    onBackClick = { viewModel.selectPlace(null) },
                    onSaveToggle = { viewModel.toggleSavePlace(selectedPlace!!) }
                )
            } else {
                Crossfade(targetState = currentDestination, label = "screen_fade") { destination ->
                    when (destination) {
                        AppDestination.HOME -> HomeScreen(
                            places = allPlaces,
                            selectedCategory = selectedCategory,
                            onSelectCategory = { viewModel.setSelectedCategory(it) },
                            onPlaceClick = { viewModel.selectPlace(it) },
                            onSaveToggle = { viewModel.toggleSavePlace(it) },
                            onExploreClick = { viewModel.setDestination(AppDestination.EXPLORE) },
                            onSearchClick = { viewModel.setDestination(AppDestination.EXPLORE) }
                        )
                        AppDestination.EXPLORE -> ExploreScreen(
                            places = allPlaces,
                            searchQuery = searchQuery,
                            searchResults = searchResults,
                            isSearching = isSearching,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = selectedCategory,
                            onCategorySelect = { viewModel.setSelectedCategory(it) },
                            onPlaceClick = { viewModel.selectPlace(it) },
                            onSaveToggle = { viewModel.toggleSavePlace(it) }
                        )
                        AppDestination.MAP -> MapScreen(
                            places = allPlaces,
                            onPlaceSelected = { viewModel.selectPlace(it) }
                        )
                        AppDestination.STORIES -> StoriesScreen(
                            stories = stories
                        )
                        AppDestination.REELS -> ReelsScreen()
                        AppDestination.SAVED -> SavedScreen(
                            savedPlaces = savedPlaces,
                            onPlaceClick = { viewModel.selectPlace(it) },
                            onRemoveSave = { viewModel.toggleSavePlace(it) }
                        )
                        AppDestination.PROFILE -> ProfileScreen(
                            places = allPlaces,
                            onPlaceClick = { viewModel.selectPlace(it) },
                            onSaveToggle = { viewModel.toggleSavePlace(it) },
                            onApiSettingsClick = { viewModel.setApiDialogVisible(true) }
                        )
                    }
                }
            }
        }

        // Dialogs
        if (showApiDialog) {
            ApiSettingsDialog(
                initialConfig = apiConfig,
                onDismiss = { viewModel.setApiDialogVisible(false) },
                onSave = { updated -> viewModel.saveApiConfig(updated) }
            )
        }

        if (showAddPlace) {
            AddPlaceDialog(
                onDismiss = { viewModel.setAddPlaceVisible(false) },
                onSubmit = { name, desc, cat, st, lat, lon, time, safe ->
                    viewModel.addNewPlace(name, desc, cat, st, lat, lon, time, safe)
                }
            )
        }

        if (showChat) {
            ChatDialog(
                contacts = chatContacts,
                onDismiss = { viewModel.setChatVisible(false) }
            )
        }
    }
}
