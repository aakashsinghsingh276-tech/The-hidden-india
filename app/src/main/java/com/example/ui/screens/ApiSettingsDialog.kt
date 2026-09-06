package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.data.util.ApiConfig
import com.example.ui.theme.*

@Composable
fun ApiSettingsDialog(
    initialConfig: ApiConfig,
    onDismiss: () -> Unit,
    onSave: (ApiConfig) -> Unit
) {
    var mapplsKey by remember { mutableStateOf(initialConfig.mapplsStaticKey) }
    var nominatimUrl by remember { mutableStateOf(initialConfig.nominatimEndpoint) }
    var openFreeMapUrl by remember { mutableStateOf(initialConfig.openFreeMapStyle) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("api_settings_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = MinimalIndigoBg,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.VpnKey, contentDescription = null, tint = MinimalIndigoBrand, modifier = Modifier.size(18.dp))
                            }
                        }
                        Text("4 APIs & Services", color = MinimalNavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = MinimalTextMuted)
                    }
                }

                Text(
                    text = "Configure your 4 real APIs as needed. India Post DIGIPIN and OpenFreeMap are pre-wired and active.",
                    color = MinimalTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                // 1. India Post DIGIPIN
                ApiCard(
                    name = "1. India Post DIGIPIN",
                    badge = "ACTIVE (OFFLINE-READY)",
                    badgeColor = ScoreUltra,
                    description = "Official national geo-addressing grid algorithm generating 10-character alphanumeric location codes for any GPS point in India.",
                    statusIcon = Icons.Filled.CheckCircle
                )

                // 2. Nominatim Geocoding API
                ApiCard(
                    name = "2. OpenStreetMap Nominatim",
                    badge = "ACTIVE (REAL REST API)",
                    badgeColor = ScoreUltra,
                    description = "Live REST API providing real geocoding search for Indian villages, mountains, and destinations.",
                    statusIcon = Icons.Filled.CloudDone
                ) {
                    OutlinedTextField(
                        value = nominatimUrl,
                        onValueChange = { nominatimUrl = it },
                        label = { Text("Endpoint URL", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                            focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 3. Mappls (MapmyIndia)
                ApiCard(
                    name = "3. Mappls (MapmyIndia)",
                    badge = if (mapplsKey.isNotBlank()) "CONFIGURED" else "KEY OPTIONAL",
                    badgeColor = if (mapplsKey.isNotBlank()) ScoreUltra else MinimalAmberStar,
                    description = "Supports Mappls eLoc routing and static map tiles. Enter your Mappls API key below:",
                    statusIcon = Icons.Filled.Key
                ) {
                    OutlinedTextField(
                        value = mapplsKey,
                        onValueChange = { mapplsKey = it },
                        placeholder = { Text("Paste your Mappls REST API Key here", color = MinimalTextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                            focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 4. OpenFreeMap
                ApiCard(
                    name = "4. OpenFreeMap Tiles",
                    badge = "ACTIVE (FREE & OPEN)",
                    badgeColor = ScoreUltra,
                    description = "High-speed free vector map tiles. No API key required.",
                    statusIcon = Icons.Filled.Map
                ) {
                    OutlinedTextField(
                        value = openFreeMapUrl,
                        onValueChange = { openFreeMapUrl = it },
                        label = { Text("Tile Style Endpoint", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                            focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                        modifier = Modifier.weight(0.4f)
                    ) {
                        Text("Cancel", color = MinimalTextSecondary)
                    }

                    Button(
                        onClick = {
                            val updated = initialConfig.copy(
                                mapplsStaticKey = mapplsKey.trim(),
                                nominatimEndpoint = nominatimUrl.trim(),
                                openFreeMapStyle = openFreeMapUrl.trim(),
                                isMapplsActive = mapplsKey.trim().isNotBlank()
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MinimalNavyPrimary, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(0.6f)
                    ) {
                        Text("Save APIs", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ApiCard(
    name: String,
    badge: String,
    badgeColor: Color,
    description: String,
    statusIcon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MinimalPillBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = statusIcon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(16.dp))
                    Text(text = name, color = MinimalNavyPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = badge,
                        color = badgeColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(text = description, color = MinimalTextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
            if (content != null) {
                Spacer(modifier = Modifier.height(2.dp))
                content()
            }
        }
    }
}
