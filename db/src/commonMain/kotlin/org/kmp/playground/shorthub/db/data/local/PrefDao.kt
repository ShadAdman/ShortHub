package org.kmp.playground.shorthub.db.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePrefs(prefs: PrefEntity)

    @Query("SELECT * FROM Prefs WHERE id = 0")
    fun getPrefs(): Flow<PrefEntity?>
}
