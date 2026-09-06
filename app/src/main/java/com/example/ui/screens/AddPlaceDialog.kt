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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.util.DigipinUtil
import com.example.ui.theme.*

@Composable
fun AddPlaceDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, description: String, category: String, state: String, lat: Double, lon: Double, bestTime: String, safety: String) -> Unit
) {
    var step by remember { mutableStateOf(1) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Mountains") }
    var state by remember { mutableStateOf("Himachal Pradesh") }
    var latText by remember { mutableStateOf("31.6366") }
    var lonText by remember { mutableStateOf("77.3456") }
    var bestTime by remember { mutableStateOf("March – June") }
    var safety by remember { mutableStateOf("Moderate. Carry warm trekking gear.") }

    val lat = latText.toDoubleOrNull() ?: 28.6139
    val lon = lonText.toDoubleOrNull() ?: 77.2090
    val generatedDigipin = remember(lat, lon) { DigipinUtil.encode(lat, lon) }
    val generatedEloc = remember(lat, lon) { DigipinUtil.generateMapplseLoc(lat, lon) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_place_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
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
                                Icon(Icons.Filled.AddLocation, contentDescription = null, tint = MinimalIndigoBrand, modifier = Modifier.size(18.dp))
                            }
                        }
                        Text("Add Hidden Place", color = MinimalNavyPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = MinimalTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stepper 1 Basic Info -> 2 Location -> 3 Safety
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StepChip(number = "1", label = "Basic Info", active = step == 1)
                    StepChip(number = "2", label = "Location", active = step == 2)
                    StepChip(number = "3", label = "Safety", active = step == 3)
                }

                Spacer(modifier = Modifier.height(18.dp))

                when (step) {
                    1 -> {
                        // Step 1: Basic Info
                        Text("Place Name *", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("e.g. Tirthan Secret Falls", color = MinimalTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Description *", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Tell us about this hidden gem...", color = MinimalTextMuted, fontSize = 13.sp) },
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Category", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Mountains", "Waterfalls", "Villages", "Historical").forEach { cat ->
                                val isSelected = category == cat
                                Surface(
                                    onClick = { category = cat },
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (isSelected) MinimalNavyPrimary else MinimalPillBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MinimalNavyPrimary else MinimalSlateBorder)
                                ) {
                                    Text(
                                        text = cat,
                                        color = if (isSelected) Color.White else MinimalTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        // Step 2: Location
                        Text("State *", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state,
                            onValueChange = { state = it },
                            placeholder = { Text("e.g. Himachal Pradesh", color = MinimalTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Latitude", color = MinimalTextSecondary, fontSize = 11.sp)
                                OutlinedTextField(
                                    value = latText,
                                    onValueChange = { latText = it },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                        focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Longitude", color = MinimalTextSecondary, fontSize = 11.sp)
                                OutlinedTextField(
                                    value = lonText,
                                    onValueChange = { lonText = it },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                        focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Live Generated DIGIPIN and eLoc preview box
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = MinimalPillBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Generated India Post DIGIPIN:", color = MinimalIndigoBrand, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = generatedDigipin,
                                    color = MinimalNavyPrimary,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text("Mappls eLoc: $generatedEloc", color = MinimalTextLight, fontSize = 11.sp)
                            }
                        }
                    }
                    3 -> {
                        // Step 3: Best Time & Safety
                        Text("Best Time to Visit", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = bestTime,
                            onValueChange = { bestTime = it },
                            placeholder = { Text("e.g. October – April", color = MinimalTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Safety Advice", color = MinimalTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = safety,
                            onValueChange = { safety = it },
                            placeholder = { Text("e.g. Carry microspikes, check rain warnings", color = MinimalTextMuted, fontSize = 13.sp) },
                            singleLine = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Stepper Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step-- },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                            modifier = Modifier.weight(0.4f)
                        ) {
                            Text("Back", color = MinimalTextSecondary)
                        }
                    }

                    Button(
                        onClick = {
                            if (step < 3) {
                                step++
                            } else {
                                if (name.isNotBlank() && description.isNotBlank()) {
                                    onSubmit(name, description, category, state, lat, lon, bestTime, safety)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MinimalNavyPrimary, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (step < 3) "Next →" else "Save Place",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepChip(number: String, label: String, active: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier.size(22.dp),
            shape = CircleShape,
            color = if (active) MinimalNavyPrimary else MinimalPillBg
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(number, color = if (active) Color.White else MinimalTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(label, color = if (active) MinimalNavyPrimary else MinimalTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
