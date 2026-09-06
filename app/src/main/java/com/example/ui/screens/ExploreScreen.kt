package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaceEntity
import com.example.data.remote.NominatimResult
import com.example.ui.components.PlaceCard
import com.example.ui.theme.*

@Composable
fun ExploreScreen(
    places: List<PlaceEntity>,
    searchQuery: String,
    searchResults: List<NominatimResult>,
    isSearching: Boolean,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    onPlaceClick: (PlaceEntity) -> Unit,
    onSaveToggle: (PlaceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedState by remember { mutableStateOf<String?>(null) }
    var sortByHiddenScore by remember { mutableStateOf(true) }

    val categoryFilters = listOf("All", "Mountains", "Waterfalls", "Villages", "Historical", "Forests")

    // Filter places
    val filteredPlaces = places.filter { place ->
        val matchesCategory = selectedCategory == null || place.category.equals(selectedCategory, ignoreCase = true)
        val matchesState = selectedState == null || place.state.contains(selectedState!!, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                place.name.contains(searchQuery, ignoreCase = true) ||
                place.state.contains(searchQuery, ignoreCase = true) ||
                place.category.contains(searchQuery, ignoreCase = true) ||
                place.digipin.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesState && matchesSearch
    }.sortedByDescending { if (sortByHiddenScore) it.hiddenScore.toFloat() else it.rating }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .padding(horizontal = 16.dp)
            .padding(bottom = 84.dp)
            .testTag("explore_screen")
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Explore Hidden Places",
                    color = MinimalNavyPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${filteredPlaces.size} locations discovered",
                    color = MinimalTextLight,
                    fontSize = 12.sp
                )
            }
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = MinimalIndigoBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalIndigoBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("HI", color = MinimalIndigoBrand, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Search Input Bar with real-time feedback
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search places, states, DIGIPIN...", color = MinimalTextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = MinimalIndigoBrand, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = MinimalTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MinimalIndigoBrand,
                unfocusedBorderColor = MinimalSlateBorder,
                focusedTextColor = MinimalTextPrimary,
                unfocusedTextColor = MinimalTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("explore_search_input")
        )

        // Live Nominatim search suggestions dropdown
        if (isSearching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                color = MinimalIndigoBrand,
                trackColor = MinimalIndigoBg
            )
        } else if (searchResults.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                shadowElevation = 3.dp
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Nominatim Geocoding Results (India):",
                        color = MinimalAmberStar,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    searchResults.take(3).forEach { result ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSearchQueryChange(result.displayName.split(",").firstOrNull() ?: searchQuery)
                                }
                                .padding(horizontal = 6.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.LocationSearching, contentDescription = null, tint = MinimalIndigoBrand, modifier = Modifier.size(16.dp))
                            Text(
                                text = result.displayName,
                                color = MinimalTextPrimary,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Filter Pills (Clean Minimalism design)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryFilters.forEach { category ->
                val isSelected = if (category == "All") selectedCategory == null else selectedCategory == category
                Surface(
                    onClick = {
                        if (category == "All") onCategorySelect(null) else onCategorySelect(category)
                    },
                    shape = RoundedCornerShape(999.dp),
                    color = if (isSelected) MinimalNavyPrimary else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MinimalNavyPrimary else MinimalSlateBorder),
                    shadowElevation = if (isSelected) 2.dp else 0.dp,
                    modifier = Modifier.testTag("filter_chip_$category")
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else MinimalTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // State & Sort Dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sort Toggle
            Surface(
                onClick = { sortByHiddenScore = !sortByHiddenScore },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                modifier = Modifier.testTag("sort_toggle_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.Tune, contentDescription = null, tint = MinimalIndigoBrand, modifier = Modifier.size(14.dp))
                    Text(
                        text = if (sortByHiddenScore) "Sort: Hidden Score ↓" else "Sort: Top Rated ↓",
                        color = MinimalTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // State Filter Chip
            Surface(
                onClick = {
                    selectedState = when (selectedState) {
                        null -> "Himachal"
                        "Himachal" -> "Uttarakhand"
                        "Uttarakhand" -> "Karnataka"
                        else -> null
                    }
                },
                shape = RoundedCornerShape(12.dp),
                color = if (selectedState != null) MinimalIndigoBg else Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedState != null) MinimalIndigoBorder else MinimalSlateBorder),
                modifier = Modifier.testTag("state_filter_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.FilterList, contentDescription = null, tint = MinimalIndigoBrand, modifier = Modifier.size(14.dp))
                    Text(
                        text = if (selectedState == null) "State: All ▾" else "State: $selectedState ✕",
                        color = if (selectedState != null) MinimalIndigoBrand else MinimalTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Places Grid (2 columns matching image)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredPlaces) { place ->
                PlaceCard(
                    place = place,
                    onClick = { onPlaceClick(place) },
                    onSaveToggle = { onSaveToggle(place) }
                )
            }
        }
    }
}
