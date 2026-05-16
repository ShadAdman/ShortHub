package org.kmp.playground.shorthub.db.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shortcut: ShortcutEntity)

    @Query("SELECT * FROM Shortcuts WHERE title LIKE '%' || :query || '%'")
    fun searchShortcuts(query: String): Flow<List<ShortcutEntity>>
}
