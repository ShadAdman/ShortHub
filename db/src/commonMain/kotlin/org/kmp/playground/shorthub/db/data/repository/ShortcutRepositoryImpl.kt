package org.kmp.playground.shorthub.db.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kmp.playground.shorthub.db.data.local.PrefDao
import org.kmp.playground.shorthub.db.data.local.PrefEntity
import org.kmp.playground.shorthub.db.data.local.ShortcutDao
import org.kmp.playground.shorthub.db.data.local.ShortcutEntity
import org.kmp.playground.shorthub.db.domain.model.Shortcut
import org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository

class ShortcutRepositoryImpl(
    private val shortcutDao: ShortcutDao,
    private val prefDao: PrefDao
) : ShortcutRepository {

    override suspend fun addShortcut(shortcut: Shortcut) {
        shortcutDao.insert(shortcut.toEntity())
    }

    override fun searchShortcuts(query: String): Flow<List<Shortcut>> {
        return shortcutDao.searchShortcuts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun savePrefs(prefs: ShortcutPrefs) {
        prefDao.savePrefs(prefs.toEntity())
    }

    override fun getPrefs(): Flow<ShortcutPrefs> {
        return prefDao.getPrefs().map { it?.toDomain() ?: ShortcutPrefs() }
    }

    private fun Shortcut.toEntity() = ShortcutEntity(
        id = id,
        title = title,
        shortcut = shortcut
    )

    private fun ShortcutEntity.toDomain() = Shortcut(
        id = id,
        title = title,
        shortcut = shortcut
    )

    private fun ShortcutPrefs.toEntity() = PrefEntity(
        addNewShortcut = addNewShortcut,
        searchShortcut = searchShortcut
    )

    private fun PrefEntity.toDomain() = ShortcutPrefs(
        addNewShortcut = addNewShortcut,
        searchShortcut = searchShortcut
    )
}
