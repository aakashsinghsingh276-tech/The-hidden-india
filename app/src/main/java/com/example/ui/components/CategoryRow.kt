package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class CategoryItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

val defaultCategories = listOf(
    CategoryItem("Mountains", Icons.Filled.Terrain, Color(0xFF06B6D4)),
    CategoryItem("Waterfalls", Icons.Filled.WaterDrop, Color(0xFF3B82F6)),
    CategoryItem("Villages", Icons.Filled.Home, Color(0xFFF97316)),
    CategoryItem("Historical", Icons.Filled.AccountBalance, Color(0xFFEF4444)),
    CategoryItem("Forests", Icons.Filled.Park, Color(0xFF10B981)),
    CategoryItem("Beaches", Icons.Filled.BeachAccess, Color(0xFF0EA5E9)),
    CategoryItem("Adventure", Icons.Filled.Hiking, Color(0xFFF43F5E))
)

@Composable
fun CategoryRow(
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        defaultCategories.forEach { category ->
            val isSelected = selectedCategory == category.name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onSelectCategory(if (isSelected) null else category.name)
                    }
                    .padding(vertical = 4.dp, horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                Brush.linearGradient(listOf(category.color, BrandGreen))
                            } else {
                                Brush.radialGradient(
                                    listOf(category.color.copy(alpha = 0.25f), DarkSurfaceVariant)
                                )
                            }
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color.White else category.color.copy(alpha = 0.4f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = if (isSelected) Color.White else category.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.name,
                    color = if (isSelected) BrandGreen else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}
