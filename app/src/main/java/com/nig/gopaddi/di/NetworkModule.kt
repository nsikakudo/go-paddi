package com.nig.gopaddi.di

import android.content.Context
import androidx.room.Room
import com.nig.gopaddi.core.network.ConnectivityNetworkMonitor
import com.nig.gopaddi.core.network.NetworkMonitor
import com.nig.gopaddi.core.services.TripApi
import com.nig.gopaddi.data.local.TripDao
import com.nig.gopaddi.data.local.TripDatabase
import com.nig.gopaddi.domain.repository.TripRepository
import com.nig.gopaddi.domain.repository.TripRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindNetworkMonitor(impl: ConnectivityNetworkMonitor): NetworkMonitor

    companion object {
        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(interceptor).build()

        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl("https://gopaddi-trips.free.beeceptor.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideTripApi(retrofit: Retrofit): TripApi = retrofit.create(TripApi::class.java)

        @Provides
        @Singleton
        fun provideTripRepository(
            api: TripApi,
            tripDao: TripDao,
            networkMonitor: NetworkMonitor
        ): TripRepository = TripRepositoryImpl(api, tripDao, networkMonitor)


        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): TripDatabase {
            return Room.databaseBuilder(context, TripDatabase::class.java, "gopaddi_db").build()
        }

        @Provides
        fun provideTripDao(db: TripDatabase): TripDao = db.tripDao()
    }
}

