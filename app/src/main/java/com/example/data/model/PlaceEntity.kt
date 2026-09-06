package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hidden_places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val longDescription: String,
    val category: String,
    val state: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val digipin: String,
    val mapplseLoc: String,
    val hiddenScore: Int,
    val beauty: Int,
    val crowd: Int,
    val accessibility: Int,
    val adventure: Int,
    val uniqueness: Int,
    val bestTimeToVisit: String,
    val safetyInfo: String,
    val rating: Float,
    val reviewsCount: Int,
    val savesCount: Int,
    val isSaved: Boolean = false,
    val imageDrawableName: String = "",
    val addedBy: String = "Aakash Singh"
)

data class ReviewItem(
    val id: String,
    val authorName: String,
    val authorLevel: Int,
    val rating: Float,
    val date: String,
    val comment: String
)

data class StoryPost(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val location: String,
    val timeAgo: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val imageDrawableName: String = "",
    val isVideo: Boolean = false,
    val durationText: String = "0:45"
)
