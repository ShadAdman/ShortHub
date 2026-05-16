package org.kmp.playground.shorthub.db.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShortcutEntity::class, PrefEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shortcutDao(): ShortcutDao
    abstract fun prefDao(): PrefDao
}
