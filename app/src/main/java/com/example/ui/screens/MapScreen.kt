package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlaceEntity
import com.example.ui.components.ScoreBadge
import com.example.ui.theme.*

@Composable
fun MapScreen(
    places: List<PlaceEntity>,
    onPlaceSelected: (PlaceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedPlaceOnMap by remember { mutableStateOf<PlaceEntity?>(places.firstOrNull()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf("All") }

    // Pulsing animation for Current Focus Marker
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_marker")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Bounding Box for India projection
    val minLat = 8.0
    val maxLat = 36.0
    val minLon = 68.0
    val maxLon = 97.0

    val filteredPlaces = remember(places, selectedTier) {
        when (selectedTier) {
            "Ultra" -> places.filter { it.hiddenScore >= 86 }
            "Very" -> places.filter { it.hiddenScore in 71..85 }
            "Hidden" -> places.filter { it.hiddenScore in 51..70 }
            else -> places
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .testTag("map_screen")
    ) {
        // Map Surface with Clean Minimalism radial dot matrix (#E2E8F0 canvas, #94A3B8 dots)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE2E8F0))
                .pointerInput(filteredPlaces) {
                    detectTapGestures { tapOffset ->
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val closest = filteredPlaces.minByOrNull { place ->
                            val normX = (place.longitude - minLon) / (maxLon - minLon)
                            val normY = 1.0 - ((place.latitude - minLat) / (maxLat - minLat))
                            val pinX = (canvasWidth * 0.15f + normX * canvasWidth * 0.7f).toFloat()
                            val pinY = (canvasHeight * 0.15f + normY * canvasHeight * 0.65f).toFloat()
                            Math.hypot((tapOffset.x - pinX).toDouble(), (tapOffset.y - pinY).toDouble())
                        }
                        if (closest != null) {
                            selectedPlaceOnMap = closest
                        }
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // 20px dot matrix background
            val dotColor = Color(0xFF94A3B8).copy(alpha = 0.35f)
            val step = 20.dp.toPx()
            val dotRadius = 1.2.dp.toPx()

            var x = 0f
            while (x < width) {
                var y = 0f
                while (y < height) {
                    drawCircle(
                        color = dotColor,
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                    y += step
                }
                x += step
            }

            // India Simplified Silhouette in pristine light slate (#CBD5E1 fill, #94A3B8 border)
            val indiaMapPath = Path().apply {
                moveTo(width * 0.40f, height * 0.16f)
                lineTo(width * 0.50f, height * 0.15f)
                lineTo(width * 0.56f, height * 0.22f)
                lineTo(width * 0.68f, height * 0.28f)
                lineTo(width * 0.85f, height * 0.27f)
                lineTo(width * 0.82f, height * 0.35f)
                lineTo(width * 0.70f, height * 0.38f)
                lineTo(width * 0.65f, height * 0.52f)
                lineTo(width * 0.58f, height * 0.68f)
                lineTo(width * 0.49f, height * 0.78f)
                lineTo(width * 0.42f, height * 0.68f)
                lineTo(width * 0.36f, height * 0.52f)
                lineTo(width * 0.28f, height * 0.42f)
                lineTo(width * 0.26f, height * 0.36f)
                lineTo(width * 0.35f, height * 0.28f)
                close()
            }

            drawPath(
                path = indiaMapPath,
                color = Color(0xFFF1F5F9)
            )
            drawPath(
                path = indiaMapPath,
                color = Color(0xFFCBD5E1),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Location Pins
            filteredPlaces.forEach { place ->
                val normX = (place.longitude - minLon) / (maxLon - minLon)
                val normY = 1.0 - ((place.latitude - minLat) / (maxLat - minLat))

                val pinX = (width * 0.15f + normX * width * 0.7f).toFloat()
                val pinY = (height * 0.15f + normY * height * 0.65f).toFloat()

                val pinColor = when {
                    place.hiddenScore >= 86 -> ScoreUltra
                    place.hiddenScore >= 71 -> ScoreVery
                    place.hiddenScore >= 51 -> ScoreHidden
                    place.hiddenScore >= 31 -> ScoreInteresting
                    else -> ScoreCommon
                }

                val isSelected = selectedPlaceOnMap?.id == place.id

                if (!isSelected) {
                    // Standard location pin
                    drawCircle(
                        color = Color.White,
                        radius = 8.dp.toPx(),
                        center = Offset(pinX, pinY)
                    )
                    drawCircle(
                        color = pinColor,
                        radius = 6.dp.toPx(),
                        center = Offset(pinX, pinY)
                    )
                }
            }
        }

        // Active Focus Marker (Placed in Compose for interactive animations and micro-badge)
        selectedPlaceOnMap?.let { place ->
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = maxWidth
                val canvasHeight = maxHeight
                val normX = (place.longitude - minLon) / (maxLon - minLon)
                val normY = 1.0 - ((place.latitude - minLat) / (maxLat - minLat))
                val pinX = canvasWidth * 0.15f + (normX * canvasWidth.value * 0.7f).dp
                val pinY = canvasHeight * 0.15f + (normY * canvasHeight.value * 0.65f).dp

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .offset(x = pinX - 44.dp, y = pinY - 48.dp)
                        .clickable { onPlaceSelected(place) }
                ) {
                    // Red Pulsing Ring with inner white dot (matching HTML design)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(MinimalRedMarker.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // "CURRENT FOCUS" Badge (matching HTML: text-[10px] font-bold text-slate-700 uppercase)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                        shadowElevation = 3.dp
                    ) {
                        Text(
                            text = "CURRENT FOCUS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MinimalTextSecondary,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Top Search Bar & Score Tier Chips (Clean Minimalism Style)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.95f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MinimalIndigoBrand,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (searchQuery.isEmpty()) "Search places (OpenFreeMap + Nominatim)..." else searchQuery,
                        color = if (searchQuery.isEmpty()) MinimalTextMuted else MinimalTextPrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.Layers,
                        contentDescription = "Map Style",
                        tint = MinimalTextLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score Tier Filters
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    Pair("All", null),
                    Pair("Ultra", ScoreUltra),
                    Pair("Very", ScoreVery),
                    Pair("Hidden", ScoreHidden)
                ).forEach { (tier, color) ->
                    val isSelected = selectedTier == tier
                    Surface(
                        onClick = { selectedTier = tier },
                        shape = RoundedCornerShape(999.dp),
                        color = if (isSelected) MinimalNavyPrimary else Color.White.copy(alpha = 0.95f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MinimalNavyPrimary else MinimalSlateBorder),
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (color != null) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                            }
                            Text(
                                text = tier,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MinimalTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Buttons (Bottom Right, matching HTML design)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 270.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Button 1: Center / Recenter (bg-[#D3E3FD] text-[#041E49] rounded-2xl w-14 h-14)
            Surface(
                onClick = {
                    selectedPlaceOnMap = places.firstOrNull()
                    Toast.makeText(context, "Centered on ${selectedPlaceOnMap?.name ?: "Location"}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(52.dp)
                    .testTag("map_center_button"),
                shape = RoundedCornerShape(18.dp),
                color = MinimalSkyContainer,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.MyLocation,
                        contentDescription = "Recenter",
                        tint = MinimalNavySecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Button 2: Layers / Map Style (bg-white text-slate-800 rounded-2xl w-14 h-14)
            Surface(
                onClick = {
                    Toast.makeText(context, "OpenFreeMap Vector Tiles: Liberty Style Active", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Layers,
                        contentDescription = "Map Style",
                        tint = MinimalTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Selected Place Bottom Sheet Card (Matching HTML specification exactly)
        selectedPlaceOnMap?.let { place ->
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .testTag("map_bottom_sheet"),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 16.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateLightBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                ) {
                    // Gray Handle Bar (w-12 h-1.5 bg-slate-200 rounded-full mx-auto mb-4)
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFE2E8F0))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title & Coordinates Tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onPlaceSelected(place) }
                        ) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MinimalTextPrimary,
                                    fontSize = 22.sp
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${place.city}, ${place.state}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MinimalTextLight,
                                    fontSize = 13.sp
                                ),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Coordinate Tag (px-3 py-1.5 bg-indigo-50 text-indigo-700 uppercase rounded-md border border-indigo-100)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MinimalIndigoBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalIndigoBorder)
                        ) {
                            Text(
                                text = "${String.format("%.4f", place.latitude)}° N, ${String.format("%.4f", place.longitude)}° E",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinimalIndigoBrand,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2-Column Grid: DIGIPIN and Mappls eLoc
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: DIGIPIN Code
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MinimalPillBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateLightBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "DIGIPIN CODE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MinimalTextMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = place.digipin,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MinimalNavyPrimary
                                )
                            }
                        }

                        // Card 2: Mappls eLoc
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MinimalPillBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateLightBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "MAPPLS ELOC",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MinimalTextMuted,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = place.mapplseLoc,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MinimalNavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons Row: Get Directions & Bookmark & Details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val uri = Uri.parse("geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}(${place.name})")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Directions: ${place.name}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("map_directions_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MinimalNavyPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Directions,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Get Directions",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }

                        // Bookmark Action
                        Surface(
                            onClick = { onPlaceSelected(place) },
                            modifier = Modifier
                                .size(50.dp)
                                .testTag("map_details_button"),
                            shape = RoundedCornerShape(16.dp),
                            color = MinimalPillBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Details",
                                    tint = MinimalTextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
