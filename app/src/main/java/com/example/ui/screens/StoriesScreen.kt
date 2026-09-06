package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StoryPost
import com.example.ui.theme.*

@Composable
fun StoriesScreen(
    stories: List<StoryPost>,
    modifier: Modifier = Modifier
) {
    val storyAvatars = listOf(
        Pair("Your Story", "+"),
        Pair("Ankit", "A"),
        Pair("TravelVibes", "T"),
        Pair("Himalayan", "H"),
        Pair("NatureLover", "N")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBgLight)
            .padding(horizontal = 16.dp)
            .padding(bottom = 84.dp)
            .testTag("stories_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Stories",
                color = MinimalNavyPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = MinimalNavyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null,
                            tint = MinimalNavyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Horizontal Story Circles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            storyAvatars.forEachIndexed { index, (name, letter) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = if (index == 0) MinimalPillBg else MinimalIndigoBg,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 2.dp,
                            color = if (index == 0) MinimalSlateBorder else MinimalIndigoBrand
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = letter,
                                color = if (index == 0) MinimalTextSecondary else MinimalIndigoBrand,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = name,
                        color = MinimalTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feed of traveler story posts
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(stories) { story ->
                StoryFeedCard(story = story)
            }
        }
    }
}

@Composable
private fun StoryFeedCard(story: StoryPost) {
    val context = LocalContext.current
    val imageResId = if (story.imageDrawableName.isNotBlank()) {
        context.resources.getIdentifier(story.imageDrawableName, "drawable", context.packageName)
    } else 0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalSlateBorder),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(38.dp),
                        shape = CircleShape,
                        color = MinimalIndigoBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = story.authorName.take(1),
                                color = MinimalIndigoBrand,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column {
                        Text(
                            text = story.authorName,
                            color = MinimalTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${story.timeAgo} · ${story.location}",
                            color = MinimalTextLight,
                            fontSize = 11.sp
                        )
                    }
                }
                Icon(Icons.Filled.MoreVert, contentDescription = null, tint = MinimalTextMuted)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Story Media Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MinimalPillBg)
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Landscape, contentDescription = null, tint = MinimalTextMuted, modifier = Modifier.size(48.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Caption
            Text(
                text = story.caption,
                color = MinimalTextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Like", tint = Color(0xFFE11D48), modifier = Modifier.size(18.dp))
                    Text(text = "${story.likesCount}", color = MinimalTextSecondary, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment", tint = MinimalTextSecondary, modifier = Modifier.size(18.dp))
                    Text(text = "${story.commentsCount}", color = MinimalTextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}
