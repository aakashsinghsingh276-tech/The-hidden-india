package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaceEntity
import com.example.ui.components.CategoryRow
import com.example.ui.components.PlaceCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    places: List<PlaceEntity>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    onPlaceClick: (PlaceEntity) -> Unit,
    onSaveToggle: (PlaceEntity) -> Unit,
    onExploreClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val heroResId = context.resources.getIdentifier("hero_hidden_india_1788714994728", "drawable", context.packageName)

    val filteredPlaces = if (selectedCategory != null) {
        places.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    } else {
        places
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
            .testTag("home_screen")
    ) {
        // Quick Search Bar (Clean Minimalist pill with light slate border)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onSearchClick() }
                .testTag("home_search_bar"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MinimalIndigoBrand,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Search places, states, or DIGIPIN...",
                    color = MinimalTextMuted,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Hero Banner with Clean Minimalism styling
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(210.dp)
                .testTag("home_hero_banner"),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 3.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (heroResId != 0) {
                    Image(
                        painter = painterResource(id = heroResId),
                        contentDescription = "Hero Landscape",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MinimalNavyPrimary)
                    )
                }

                // Smooth dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.50f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Hero Text & Clean Minimalism Button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Discover\nHidden Places\nof India",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Unexplored trails, secret waterfalls, peaceful\nvillages, ancient temples and more...",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MinimalNavyPrimary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Explore Now →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Category Circular Badges
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            CategoryRow(
                selectedCategory = selectedCategory,
                onSelectCategory = onSelectCategory
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Trending Hidden Places Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending Hidden Places",
                color = MinimalNavyPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View all →",
                color = MinimalIndigoBrand,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onExploreClick() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Trending Places Horizontal Carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredPlaces) { place ->
                PlaceCard(
                    place = place,
                    onClick = { onPlaceClick(place) },
                    onSaveToggle = { onSaveToggle(place) },
                    modifier = Modifier.width(190.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Community Explorer Highlights (Clean Minimalism Card)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MinimalIndigoBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = MinimalIndigoBrand,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Not just places, it's an emotion...",
                            color = MinimalTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Powered by OpenFreeMap, Nominatim & India Post DIGIPIN",
                            color = MinimalTextLight,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FeaturePill(icon = Icons.Filled.Explore, label = "Discover")
                    FeaturePill(icon = Icons.Filled.Share, label = "Share Story")
                    FeaturePill(icon = Icons.Filled.Groups, label = "Connect")
                    FeaturePill(icon = Icons.Filled.WorkspacePremium, label = "Earn Guide")
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MinimalPillBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MinimalNavyPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = MinimalTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
