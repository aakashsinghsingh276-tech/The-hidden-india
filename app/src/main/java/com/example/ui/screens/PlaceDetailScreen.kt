package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaceEntity
import com.example.ui.components.ScoreBadge
import com.example.ui.theme.*

@Composable
fun PlaceDetailScreen(
    place: PlaceEntity,
    onBackClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Photos", "Reviews", "Safety")
    val context = LocalContext.current

    val imageResId = if (place.imageDrawableName.isNotBlank()) {
        context.resources.getIdentifier(place.imageDrawableName, "drawable", context.packageName)
    } else 0

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .testTag("place_detail_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 95.dp)
        ) {
            // Photo Hero Carousel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(MinimalPillBg)
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = place.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Landscape,
                            contentDescription = null,
                            tint = MinimalTextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Top navigation buttons on photo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("detail_back_button"),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        shadowElevation = 3.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MinimalNavyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Check out ${place.name} on Hidden Indian! DIGIPIN: ${place.digipin}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Place"))
                            },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.92f),
                            shadowElevation = 3.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share",
                                    tint = MinimalNavyPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Surface(
                            onClick = onSaveToggle,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("detail_save_button"),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.92f),
                            shadowElevation = 3.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (place.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = if (place.isSaved) MinimalIndigoBrand else MinimalTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Image counter badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.65f)
                ) {
                    Text(
                        text = "1/12",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Place Heading Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = place.name,
                            color = MinimalNavyPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = MinimalTextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${place.city}, ${place.state}",
                                color = MinimalTextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Score Badge
                    ScoreBadge(score = place.hiddenScore, showLabel = true)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature Tag Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(place.category, "Trek", "Village", "Nature").forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
                        ) {
                            Text(
                                text = "✓ $tag",
                                color = MinimalTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Tabs (Overview, Photos, Reviews, Safety)
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
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Tab Content
                when (selectedTab) {
                    0 -> OverviewTab(place = place)
                    1 -> PhotosTab(place = place)
                    2 -> ReviewsTab(place = place)
                    3 -> SafetyTab(place = place)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hidden Score Breakdown Card
                ScoreBreakdownCard(place = place)
            }
        }

        // Fixed Bottom Action CTA Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onSaveToggle,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (place.isSaved) MinimalIndigoBrand else MinimalTextPrimary
                    ),
                    border = BorderStroke(1.dp, if (place.isSaved) MinimalIndigoBrand else MinimalSlateBorder),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(0.4f)
                        .height(48.dp)
                        .testTag("detail_bottom_save_button")
                ) {
                    Icon(
                        imageVector = if (place.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (place.isSaved) "Saved" else "Save Place",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        val geoUri = Uri.parse("geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}(${place.name})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                        context.startActivity(mapIntent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MinimalNavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(0.6f)
                        .height(48.dp)
                        .testTag("detail_directions_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Get Directions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewTab(place: PlaceEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Column {
            Text(
                text = "About",
                color = MinimalNavyPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = place.longDescription,
                color = MinimalTextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        // Location Info Card with DIGIPIN and Mappls
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(
                    icon = Icons.Filled.Place,
                    title = "Location",
                    value = "${place.name}, ${place.state}"
                )
                InfoRow(
                    icon = Icons.Filled.QrCode,
                    title = "India Post DIGIPIN",
                    value = place.digipin,
                    isHighlight = true
                )
                InfoRow(
                    icon = Icons.Filled.Map,
                    title = "Mappls eLoc Code",
                    value = place.mapplseLoc,
                    isHighlight = true
                )
                InfoRow(
                    icon = Icons.Filled.CalendarMonth,
                    title = "Best Time to Visit",
                    value = place.bestTimeToVisit
                )
                InfoRow(
                    icon = Icons.Filled.Shield,
                    title = "Safety Level",
                    value = "Moderate · Verified trails and local guide network"
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isHighlight) MinimalIndigoBrand else MinimalNavyPrimary,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Column {
            Text(text = title, color = MinimalTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(
                text = value,
                color = if (isHighlight) MinimalIndigoBrand else MinimalTextPrimary,
                fontFamily = if (isHighlight) FontFamily.Monospace else FontFamily.Default,
                fontSize = 13.sp,
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ScoreBreakdownCard(place: PlaceEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Hidden Indian Algorithm Score",
                color = MinimalNavyPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Formula: (CrowdDensity × 0.4) + (Footfall × 0.3) + (Uniqueness × 0.3)",
                color = MinimalTextLight,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(4.dp))
            ScoreBar(label = "Low Crowd Density Factor", value = place.crowd)
            ScoreBar(label = "Adventure & Trail Index", value = place.adventure)
            ScoreBar(label = "Uniqueness & Natural Beauty", value = place.uniqueness)
        }
    }
}

@Composable
private fun ScoreBar(label: String, value: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = MinimalTextSecondary, fontSize = 11.sp)
            Text(text = "$value%", color = MinimalNavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MinimalIndigoBrand,
            trackColor = MinimalPillBg
        )
    }
}

@Composable
private fun PhotosTab(place: PlaceEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Traveler Community Photos (12)", color = MinimalNavyPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                shape = RoundedCornerShape(12.dp),
                color = MinimalPillBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Waterfall View", color = MinimalTextSecondary, fontSize = 12.sp)
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                shape = RoundedCornerShape(12.dp),
                color = MinimalPillBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Trek Ridge", color = MinimalTextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ReviewsTab(place: PlaceEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Reviews & Ratings", color = MinimalNavyPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Rohit Sharma · Explorer Level 4", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("★★★★★", color = MinimalAmberStar, fontSize = 12.sp)
                }
                Text(
                    "Unbelievable spot! Pure serenity with no mobile network disruptions. Make sure you use the DIGIPIN coordinate to reach the exact riverside camp.",
                    color = MinimalTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SafetyTab(place: PlaceEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Safety Guidelines & Tips", color = MinimalNavyPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(place.safetyInfo, color = MinimalTextSecondary, fontSize = 13.sp)
            Text("• Always inform local homestays before venturing into deep forest trails.", color = MinimalTextLight, fontSize = 12.sp)
            Text("• Carry offline maps or note the India Post DIGIPIN code.", color = MinimalTextLight, fontSize = 12.sp)
        }
    }
}
