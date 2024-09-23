package com.codingtho.qrgen.ui.screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtho.qrgen.data.repository.QrCodeRepository
import com.codingtho.qrgen.data.repository.model.QrCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedScreenViewModel @Inject constructor(
    private val repository: QrCodeRepository
) : ViewModel() {

    private val _qrCodes = MutableStateFlow(emptyList<QrCode>())
    val qrCodes get() = _qrCodes.asStateFlow()

    init {
        getQrCodes()
    }

    private fun getQrCodes() {
        viewModelScope.launch {
            _qrCodes.value = repository.getQrCodes()
        }
    }

    fun deleteQrCode(qr: QrCode) {
        viewModelScope.launch {
            repository.deleteQrCode(qr)
            getQrCodes()
        }
    }
}
