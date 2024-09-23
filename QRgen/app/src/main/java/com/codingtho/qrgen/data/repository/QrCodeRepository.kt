package com.codingtho.qrgen.data.repository

import com.codingtho.qrgen.data.local.dao.QrCodeDao
import com.codingtho.qrgen.data.local.entity.QrCodeEntity
import com.codingtho.qrgen.data.local.entity.toItem
import com.codingtho.qrgen.data.remote.api.ApiService
import com.codingtho.qrgen.data.repository.model.QrCode
import com.codingtho.qrgen.data.repository.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrCodeRepository @Inject constructor(
    private val api: ApiService,
    private val qrCodeDao: QrCodeDao
) {
    suspend fun createQrCode(content: String): String? {
        return try {
            val responseBody: ResponseBody = api.createQrCode(content)
            val file = withContext(Dispatchers.IO) {
                File.createTempFile("qrcode", ".png")
            }
            val outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
            outputStream.use { outputStream.write(responseBody.bytes()) }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveQrCode(title: String?, content: String, image: String) {
        val qrCode = QrCodeEntity(title = title, content = content, image = image)
        try {
            qrCodeDao.insertQrCode(qrCode)
        } catch (e: Exception) {
            return
        }
    }

    suspend fun getQrCodes(): List<QrCode> {
        return try {
            val response = qrCodeDao.getQrCodes()
            response.map { it.toItem() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteQrCode(qrCode: QrCode) {
        try {
            qrCodeDao.deleteQrCode(qrCode.toEntity())
        } catch (e: Exception) {
            return
        }
    }
}
