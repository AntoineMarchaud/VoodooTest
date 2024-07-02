package com.example.voodoosdk.data.di

import com.example.voodoosdk.data.repository.VoodooTestRepositoryImpl
import com.example.voodoosdk.domain.repository.VoodooTestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDataRepo(repository: VoodooTestRepositoryImpl): VoodooTestRepository
}