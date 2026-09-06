package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.ChatMessage

@Composable
fun ChatDialog(
    contacts: List<ChatMessage>,
    onDismiss: () -> Unit
) {
    var selectedContact by remember { mutableStateOf<ChatMessage?>(null) }
    var messageText by remember { mutableStateOf("") }
    var activeConversation by remember { mutableStateOf(contacts) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("chat_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Chat Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedContact != null) {
                            IconButton(onClick = { selectedContact = null }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MinimalNavyPrimary)
                            }
                        }
                        Column {
                            Text(
                                text = selectedContact?.senderName ?: "Explorer Chat",
                                color = MinimalNavyPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (selectedContact != null) "Online · Verified Traveler" else "Connect with fellow explorers",
                                color = MinimalIndigoBrand,
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = MinimalTextMuted)
                    }
                }

                HorizontalDivider(color = MinimalSlateBorder, modifier = Modifier.padding(vertical = 10.dp))

                if (selectedContact == null) {
                    // Contact list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(activeConversation) { contact ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedContact = contact },
                                shape = RoundedCornerShape(14.dp),
                                color = MinimalPillBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(MinimalIndigoBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = contact.avatarInitial,
                                            color = MinimalIndigoBrand,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        if (contact.isOnline) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(ScoreUltra)
                                                    .border(1.dp, Color.White, CircleShape)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = contact.senderName, color = MinimalTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(text = contact.timeAgo, color = MinimalTextLight, fontSize = 11.sp)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = contact.text, color = MinimalTextSecondary, fontSize = 12.sp, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Active conversation thread
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Received message
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MinimalPillBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
                            ) {
                                Text(
                                    text = selectedContact!!.text,
                                    color = MinimalTextPrimary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Sent confirmation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MinimalNavyPrimary
                            ) {
                                Text(
                                    text = "Hey! Planning a trip there this month. DIGIPIN coordinate is saved!",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }

                    // Input Box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Write a message...", color = MinimalTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MinimalTextPrimary, unfocusedTextColor = MinimalTextPrimary,
                                focusedBorderColor = MinimalIndigoBrand, unfocusedBorderColor = MinimalSlateBorder,
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    messageText = ""
                                }
                            },
                            shape = CircleShape,
                            color = MinimalNavyPrimary,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
