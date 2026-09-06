package com.example.data.util

import android.content.Context
import android.content.SharedPreferences

data class ApiConfig(
    val openFreeMapStyle: String = "https://tiles.openfreemap.org/styles/liberty",
    val nominatimEndpoint: String = "https://nominatim.openstreetmap.org/search",
    val nominatimUserAgent: String = "HiddenIndian-AndroidApp/1.0",
    val mapplsStaticKey: String = "",
    val digipinPrefix: String = "India Post DoP",
    val isMapplsActive: Boolean = false
)

class ApiConfigManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("api_config_prefs", Context.MODE_PRIVATE)

    fun getConfig(): ApiConfig {
        return ApiConfig(
            openFreeMapStyle = prefs.getString("open_free_map_style", "https://tiles.openfreemap.org/styles/liberty") ?: "",
            nominatimEndpoint = prefs.getString("nominatim_endpoint", "https://nominatim.openstreetmap.org/search") ?: "",
            nominatimUserAgent = prefs.getString("nominatim_user_agent", "HiddenIndian-AndroidApp/1.0") ?: "",
            mapplsStaticKey = prefs.getString("mappls_static_key", "") ?: "",
            digipinPrefix = "India Post DoP",
            isMapplsActive = prefs.getBoolean("is_mappls_active", false)
        )
    }

    fun saveConfig(config: ApiConfig) {
        prefs.edit()
            .putString("open_free_map_style", config.openFreeMapStyle)
            .putString("nominatim_endpoint", config.nominatimEndpoint)
            .putString("nominatim_user_agent", config.nominatimUserAgent)
            .putString("mappls_static_key", config.mapplsStaticKey)
            .putBoolean("is_mappls_active", config.mapplsStaticKey.isNotBlank())
            .apply()
    }
}
