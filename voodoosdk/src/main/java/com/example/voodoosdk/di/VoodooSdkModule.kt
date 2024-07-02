package com.example.voodoosdk.di

import android.content.Context
import com.example.voodoosdk.VoodooSdk
import com.example.voodoosdk.VoodooSdkImpl
import com.example.voodoosdk.domain.repository.VoodooTestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VoodooSdkModule {

    @Singleton
    @Provides
    fun provideCsAnalytics(
        @ApplicationContext appContext: Context,
        repository: VoodooTestRepository,
    ): VoodooSdk {
        return VoodooSdkImpl(repository)
    }
}