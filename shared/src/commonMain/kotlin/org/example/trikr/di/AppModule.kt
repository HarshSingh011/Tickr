package org.example.trikr.di

import org.example.trikr.data.network.TickrApiClient
import org.example.trikr.data.repositories.AuthRepositoryImpl
import org.example.trikr.data.storage.TokenStorage
import org.example.trikr.domain.repositories.AuthRepository
import org.example.trikr.data.repositories.TaskRepositoryImpl
import org.example.trikr.domain.repositories.TaskRepository
import org.koin.dsl.module

val appModule = module {
    single { TokenStorage() }
    single { TickrApiClient(get()) }
    
    // Bind the interface to its implementation
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}
