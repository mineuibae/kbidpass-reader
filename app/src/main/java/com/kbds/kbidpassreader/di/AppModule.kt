package com.kbds.kbidpassreader.di

import android.app.Service
import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import com.kbds.kbidpassreader.data.source.KBIdPassDataSource
import com.kbds.kbidpassreader.data.source.KBIdPassDefaultRepository
import com.kbds.kbidpassreader.data.source.KBIdPassRepository
import com.kbds.kbidpassreader.data.source.local.KBIdPassDatabase
import com.kbds.kbidpassreader.data.source.local.KBIdPassLocalDataSource
import com.kbds.kbidpassreader.di.qualifier.IoDispatcher
import com.kbds.kbidpassreader.di.qualifier.LocalDataSource
import com.kbds.kbidpassreader.di.qualifier.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @LocalDataSource
    @Provides
    fun provideKBIdPassLocalDataSource(
        database: KBIdPassDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): KBIdPassDataSource {
        return KBIdPassLocalDataSource(
            database.userDao(), database.logDao(), ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideKBIdPassDatabase(@ApplicationContext context: Context): KBIdPassDatabase =
        Room.databaseBuilder(context.applicationContext,
            KBIdPassDatabase::class.java, "KBIdPass.db")
            .build()

    @Singleton
    @Provides
    fun provideKBIdPassRepository(
        @LocalDataSource kbIdPassLocalDataSource: KBIdPassDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): KBIdPassRepository {
        return KBIdPassDefaultRepository(kbIdPassLocalDataSource, ioDispatcher)
    }

    @Singleton
    @IoDispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @MainDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Main

    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator =
        context.applicationContext.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

    @Provides
    fun provideJson() : Json {
        return Json { ignoreUnknownKeys = true }
    }
}

/*
@Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Provides
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
            as ClipboardManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    @MainThreadHandler
    fun provideMainThreadHandler(): IOSchedHandler = IOSchedMainHandler()

    @Singleton
    @Provides
    fun provideAnalyticsHelper(
        @ApplicationScope applicationScope: CoroutineScope,
        @ApplicationContext context: Context,
        signInDelegate: SignInViewModelDelegate,
        preferenceStorage: PreferenceStorage
    ): AnalyticsHelper =
        FirebaseAnalyticsHelper(applicationScope, context, signInDelegate, preferenceStorage)

    @Singleton
    @Provides
    fun provideAgendaRepository(appConfigDataSource: AppConfigDataSource): AgendaRepository =
        DefaultAgendaRepository(appConfigDataSource)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
 */