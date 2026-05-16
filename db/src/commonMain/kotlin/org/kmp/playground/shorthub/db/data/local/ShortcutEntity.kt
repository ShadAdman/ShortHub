package org.kmp.playground.shorthub.db.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Shortcuts")
data class ShortcutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val shortcut: String
)
