package com.codingtho.qrgen.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingtho.qrgen.data.local.entity.QrCodeEntity

@Dao
interface QrCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQrCode(qrCode: QrCodeEntity)

    @Query("SELECT * FROM qr_code_table")
    suspend fun getQrCodes(): List<QrCodeEntity>

    @Delete
    suspend fun deleteQrCode(qrCode: QrCodeEntity)
}
