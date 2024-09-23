package com.codingtho.qrgen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingtho.qrgen.data.local.dao.QrCodeDao
import com.codingtho.qrgen.data.local.entity.QrCodeEntity

@Database(entities = [QrCodeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getQrCodeDao(): QrCodeDao
}
