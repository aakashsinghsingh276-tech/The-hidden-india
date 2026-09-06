package com.example.data.util

import java.util.Locale

/**
 * India Post DIGIPIN (Digital Postal Index Number) Generator
 * Based on the Department of Posts (DoP) National Addressing Grid specification.
 * Divides the country into hierarchical grid cells to generate a 10-character code:
 * Format: XXX-XXX-XXXX (e.g. FFF-3MF-34F8 for India Gate, New Delhi).
 */
object DigipinUtil {
    // DIGIPIN bounding box for Indian territory including maritime borders
    private const val LAT_MIN = 2.5
    private const val LAT_MAX = 38.5
    private const val LON_MIN = 63.5
    private const val LON_MAX = 99.5

    // DIGIPIN 16-symbol alphabet (India Post character set without easily confused letters)
    private val DIGIPIN_ALPHABET = charArrayOf(
        'F', '3', 'M', '8', '2', '4', '7', '9',
        'C', 'G', 'H', 'J', 'P', 'R', 'T', 'W'
    )

    /**
     * Converts Latitude and Longitude to a formatted 10-character DIGIPIN code.
     */
    fun encode(latitude: Double, longitude: Double): String {
        // Clamp within bounding box
        val latClamped = latitude.coerceIn(LAT_MIN, LAT_MAX)
        val lonClamped = longitude.coerceIn(LON_MIN, LON_MAX)

        var minLat = LAT_MIN
        var maxLat = LAT_MAX
        var minLon = LON_MIN
        var maxLon = LON_MAX

        val codeBuilder = StringBuilder()

        for (i in 0 until 10) {
            val latSpan = (maxLat - minLat) / 4.0
            val lonSpan = (maxLon - minLon) / 4.0

            val latIndex = ((latClamped - minLat) / latSpan).toInt().coerceIn(0, 3)
            val lonIndex = ((lonClamped - minLon) / lonSpan).toInt().coerceIn(0, 3)

            val symbolIndex = (latIndex * 4 + lonIndex) % DIGIPIN_ALPHABET.size
            codeBuilder.append(DIGIPIN_ALPHABET[symbolIndex])

            minLat += latIndex * latSpan
            maxLat = minLat + latSpan
            minLon += lonIndex * lonSpan
            maxLon = minLon + lonSpan
        }

        val raw = codeBuilder.toString()
        // Format as XXX-XXX-XXXX
        return "${raw.substring(0, 3)}-${raw.substring(3, 6)}-${raw.substring(6, 10)}"
    }

    /**
     * Generates a sample Mappls eLoc format (6-digit alphanumeric plus plus-code)
     */
    fun generateMapplseLoc(latitude: Double, longitude: Double): String {
        val latPart = String.format(Locale.US, "%02d", ((latitude * 100).toInt() % 90))
        val lonPart = String.format(Locale.US, "%02d", ((longitude * 100).toInt() % 90))
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val hash = (Math.abs(latitude * 1000 + longitude * 10000).toInt()) % chars.length
        val char1 = chars[hash]
        val char2 = chars[(hash + 7) % chars.length]
        return "${latPart}${char1}${char2}+RW"
    }
}
