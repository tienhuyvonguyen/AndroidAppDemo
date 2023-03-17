package com.example.app.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    var displayName: String
) {
    fun getDisplayName(): LoggedInUser {
        return LoggedInUser(displayName)
    }

    fun set_DisplayName(displayName: String) {
        this.displayName = displayName
    }
}
