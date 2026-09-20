package io.horizon.vip.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mode: String,
    val target: String,
    val profile: String,
    val startedAt: Long,
    val finishedAt: Long,
    val findingsCount: Int,
    val resultJson: String
)

@Entity(tableName = "tunable_checks")
data class TunableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domain: String,
    val tunable: Boolean,
    val server: String?,
    val ip: String?,
    val reason: String,
    val checkedAt: Long
)
