package com.android.beyikyol2.feature_other.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.beyikyol2.feature_other.data.local.entity.GetHomeEntity
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto

@Dao
interface GetHomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHome(info: GetHomeEntity)

    @Query("DELETE FROM gethomeentity WHERE 1==1")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM gethomeentity")
    suspend fun getHome(): GetHomeEntity

}