package io.horizon.vip.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScanEntity::class, TunableEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HorizonDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}
