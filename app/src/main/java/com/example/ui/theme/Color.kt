package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Clean Minimalism Design System Colors
// (Adhering to the Clean Minimalism HTML/CSS specification)
// ==========================================

// Core Background & Surfaces
val MinimalBgLight = Color(0xFFF8F9FA)           // Clean canvas off-white
val MinimalSurfaceLight = Color(0xFFFFFFFF)      // Pure white card & sheet surface
val MinimalPillBg = Color(0xFFF1F5F9)            // Slate-100 pill/badge background
val MinimalSlateBorder = Color(0xFFE2E8F0)       // Slate-200 border
val MinimalSlateLightBorder = Color(0xFFF1F5F9)  // Slate-100 subtle border

// Primary Navy & Indigo Brand
val MinimalNavyPrimary = Color(0xFF001D35)       // Deep Navy for primary CTAs and headings
val MinimalNavySecondary = Color(0xFF041E49)     // Dark Blue for icon tints in Sky containers
val MinimalNavyHover = Color(0xFF002B4E)
val MinimalSkyContainer = Color(0xFFD3E3FD)      // Soft Sky Blue FAB
val MinimalIndigoBrand = Color(0xFF4338CA)       // Indigo-700 active nav & tags
val MinimalIndigoBg = Color(0xFFEEF2FF)          // Indigo-50 coordinate tag container
val MinimalIndigoBorder = Color(0xFFE0E7FF)      // Indigo-100 tag border

// Text Hierarchy
val MinimalTextPrimary = Color(0xFF0F172A)       // Slate-900 high-contrast title text
val MinimalTextSecondary = Color(0xFF475569)     // Slate-600 body text
val MinimalTextLight = Color(0xFF64748B)         // Slate-500 subtitles & metadata
val MinimalTextMuted = Color(0xFF94A3B8)         // Slate-400 placeholder & inactive icons

// Accent & Score Tier Indicators
val MinimalEmeraldGem = Color(0xFF10B981)        // Emerald-500 badge
val MinimalAmberStar = Color(0xFFF59E0B)         // Star ratings & bookmarks
val MinimalRedMarker = Color(0xFFEF4444)         // Red-500 pulsing focus marker

// Compatibility Aliases for components
val DarkBackground = MinimalBgLight
val DarkSurface = MinimalSurfaceLight
val DarkSurfaceVariant = MinimalPillBg
val DarkCard = MinimalSurfaceLight
val DarkCardBorder = MinimalSlateBorder
val DarkInputBg = MinimalPillBg

val BrandGreen = MinimalEmeraldGem
val BrandGreenDark = Color(0xFF059669)
val BrandGreenContainer = Color(0xFFD1FAE5)

val AccentGold = MinimalAmberStar
val AccentCyan = Color(0xFF0284C7)
val AccentOrange = Color(0xFFEA580C)
val AccentRose = MinimalRedMarker
val AccentPurple = Color(0xFF7C3AED)

val TextWhite = Color(0xFFFFFFFF)
val TextPrimary = MinimalTextPrimary
val TextSecondary = MinimalTextSecondary
val TextMuted = MinimalTextLight

val ScoreUltra = Color(0xFF10B981)
val ScoreVery = Color(0xFF0284C7)
val ScoreHidden = Color(0xFFF59E0B)
val ScoreInteresting = Color(0xFFEA580C)
val ScoreCommon = Color(0xFF94A3B8)

val ScoreUltraGreen = ScoreUltra
val ScoreVeryBlue = ScoreVery
val ScoreHiddenAmber = ScoreHidden
val ScoreInterestingOrange = ScoreInteresting
val ScoreCommonSlate = ScoreCommon

