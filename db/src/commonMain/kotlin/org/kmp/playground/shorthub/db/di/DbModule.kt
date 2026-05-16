package org.kmp.playground.shorthub.db.di

import org.kmp.playground.shorthub.db.data.local.AppDatabase
import org.kmp.playground.shorthub.db.data.local.getDatabaseBuilder
import org.kmp.playground.shorthub.db.data.repository.ShortcutRepositoryImpl
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }
    
    single { get<AppDatabase>().shortcutDao() }
    single { get<AppDatabase>().prefDao() }
    
    single<ShortcutRepository> { ShortcutRepositoryImpl(get(), get()) }
}
