package io.horizon.vip.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.horizon.vip.data.db.HorizonDatabase
import io.horizon.vip.data.db.ScanDao
import io.horizon.vip.data.prefs.SettingsDataStore
import io.horizon.vip.engine.ScanEngine
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HorizonDatabase {
        return Room.databaseBuilder(
            context,
            HorizonDatabase::class.java,
            "horizon_vip.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideScanDao(db: HorizonDatabase): ScanDao = db.scanDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideScanEngine(): ScanEngine = ScanEngine()
}
