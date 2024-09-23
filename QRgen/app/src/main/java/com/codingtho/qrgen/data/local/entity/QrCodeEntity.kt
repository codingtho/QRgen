package com.codingtho.qrgen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingtho.qrgen.data.repository.model.QrCode

@Entity(tableName = "qr_code_table")
data class QrCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = null,
    val content: String,
    val image: String
)

fun QrCodeEntity.toItem() = QrCode(id = id, title = title, content = content, image = image)
