package com.android.beyikyol2.feature_other.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.beyikyol2.feature_other.data.local.entity.GetHomeEntity

@Database(
    entities = [GetHomeEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class GetHomeDatabase: RoomDatabase() {
    abstract val dao: GetHomeDao
}