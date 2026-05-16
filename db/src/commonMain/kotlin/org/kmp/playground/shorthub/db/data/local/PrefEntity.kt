package org.kmp.playground.shorthub.db.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prefs")
data class PrefEntity(
    @PrimaryKey val id: Int = 0, // Single row for app preferences
    val addNewShortcut: String,
    val searchShortcut: String
)
