package com.codingtho.qrgen.data.repository.model

import com.codingtho.qrgen.data.local.entity.QrCodeEntity

data class QrCode(
    val id: Int,
    val title: String? = null,
    val content: String,
    val image: String
)

fun QrCode.toEntity() = QrCodeEntity(id = id, title = title, content = content, image = image)
