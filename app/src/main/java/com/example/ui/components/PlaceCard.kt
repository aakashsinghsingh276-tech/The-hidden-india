package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.*

@Composable
fun ScoreBadge(
    score: Int,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    val (bgColor, textColor, label) = when {
        score >= 86 -> Triple(ScoreUltra.copy(alpha = 0.12f), ScoreUltra, "Ultra Hidden")
        score >= 71 -> Triple(ScoreVery.copy(alpha = 0.12f), ScoreVery, "Very Hidden")
        score >= 51 -> Triple(ScoreHidden.copy(alpha = 0.12f), ScoreHidden, "Hidden")
        score >= 31 -> Triple(ScoreInteresting.copy(alpha = 0.12f), ScoreInteresting, "Interesting")
        else -> Triple(ScoreCommon.copy(alpha = 0.12f), ScoreCommon, "Common")
    }

    Surface(
        modifier = modifier.testTag("score_badge_${score}"),
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(textColor, CircleShape)
            )
            Text(
                text = "$score",
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (showLabel) {
                Text(
                    text = "· $label",
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: PlaceEntity,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = if (place.imageDrawableName.isNotBlank()) {
        context.resources.getIdentifier(place.imageDrawableName, "drawable", context.packageName)
    } else 0

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("place_card_${place.id}"),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            // Thumbnail Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(MinimalPillBg)
            ) {
                if (imageResId != 0) {
                    androidx.compose.foundation.Image(
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
                        Text(
                            text = place.category.take(1),
                            color = MinimalTextMuted,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Bookmark button (pill surface overlay)
                Surface(
                    onClick = onSaveToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .testTag("place_save_button_${place.id}"),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (place.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save place",
                            tint = if (place.isSaved) MinimalIndigoBrand else MinimalTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Score badge overlay on bottom right of thumbnail
                ScoreBadge(
                    score = place.hiddenScore,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }

            // Details content with Clean Minimalism typography
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = place.name,
                    color = MinimalTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "${place.city}, ${place.state}",
                        color = MinimalTextLight,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = MinimalAmberStar,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = String.format("%.1f", place.rating),
                            color = MinimalTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MinimalPillBg
                    ) {
                        Text(
                            text = place.category,
                            color = MinimalTextLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
