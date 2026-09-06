package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaceEntity
import com.example.ui.components.PlaceCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    places: List<PlaceEntity>,
    onPlaceClick: (PlaceEntity) -> Unit,
    onSaveToggle: (PlaceEntity) -> Unit,
    onApiSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("My Places", "Reviews", "Saved", "Following")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .padding(horizontal = 16.dp)
            .padding(bottom = 84.dp)
            .testTag("profile_screen")
    ) {
        // Top Action bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Explorer Profile",
                color = MinimalNavyPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Surface(
                onClick = onApiSettingsClick,
                shape = CircleShape,
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                modifier = Modifier
                    .size(38.dp)
                    .testTag("profile_settings_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MinimalNavyPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Profile Avatar, Name, Level & XP Bar (Clean Minimalism Card)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(68.dp),
                    shape = CircleShape,
                    color = MinimalIndigoBg,
                    border = androidx.compose.foundation.BorderStroke(2.dp, MinimalIndigoBrand)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "AS",
                            color = MinimalIndigoBrand,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Aakash Singh",
                    color = MinimalNavyPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Explorer Level 5 · Trail Master",
                    color = MinimalIndigoBrand,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // XP Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(0.88f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Rank: Master Scout", color = MinimalTextLight, fontSize = 11.sp)
                    Text(text = "320 / 500 XP", color = MinimalNavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { 320f / 500f },
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MinimalIndigoBrand,
                    trackColor = MinimalPillBg
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 4 Stats Blocks (48 Discovered | 17 Places Added | 31 Reviews | 94 Photos)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(count = "48", label = "Discovered")
                    StatItem(count = "17", label = "Added")
                    StatItem(count = "31", label = "Reviews")
                    StatItem(count = "94", label = "Photos")
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Badges Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Badges",
                color = MinimalNavyPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All →",
                color = MinimalIndigoBrand,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BadgePill(title = "Mountain\nExplorer", icon = Icons.Filled.Terrain, color = Color(0xFF0284C7))
            BadgePill(title = "Waterfall\nHunter", icon = Icons.Filled.WaterDrop, color = Color(0xFF2563EB))
            BadgePill(title = "Photographer\nExpert", icon = Icons.Filled.PhotoCamera, color = Color(0xFFEA580C))
            BadgePill(title = "Hidden\nHunter", icon = Icons.Filled.Stars, color = Color(0xFFDC2626))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Profile Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MinimalIndigoBrand,
            divider = { HorizontalDivider(color = MinimalSlateBorder) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) MinimalIndigoBrand else MinimalTextLight,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Places Grid for user
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(places.take(4)) { place ->
                PlaceCard(
                    place = place,
                    onClick = { onPlaceClick(place) },
                    onSaveToggle = { onSaveToggle(place) }
                )
            }
        }
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = MinimalNavyPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            color = MinimalTextLight,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun BadgePill(title: String, icon: ImageVector, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Surface(
            modifier = Modifier.size(46.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.08f),
            border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(22.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = MinimalTextSecondary,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
