package io.horizon.vip.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scan: ScanEntity): Long

    @Query("SELECT * FROM scans ORDER BY finishedAt DESC")
    fun observeAll(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scans ORDER BY finishedAt DESC LIMIT 1")
    suspend fun latest(): ScanEntity?

    @Query("SELECT * FROM scans WHERE id = :id")
    suspend fun getById(id: Long): ScanEntity?

    @Query("DELETE FROM scans WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM scans")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTunable(check: TunableEntity): Long

    @Query("SELECT * FROM tunable_checks ORDER BY checkedAt DESC LIMIT 50")
    fun observeTunable(): Flow<List<TunableEntity>>
}
