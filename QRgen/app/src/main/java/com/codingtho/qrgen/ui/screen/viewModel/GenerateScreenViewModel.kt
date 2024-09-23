package com.codingtho.qrgen.ui.screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtho.qrgen.data.repository.QrCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateScreenViewModel @Inject constructor(
    private val repository: QrCodeRepository
) : ViewModel() {

    data class UiState(
        val title: String = "",
        val content: String = "",
        val contentInputError: Boolean = false,
        val isQrCodeGenerating: Boolean = false,
        val isQrCodeGenerated: Boolean = false,
        val qrCodeImage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onContentChange(content: String) {
        _uiState.update { it.copy(content = content) }
        isContentEmpty()
    }

    private fun isContentEmpty(): Boolean {
        _uiState.update {
            it.copy(
                contentInputError =
                _uiState.value.content.isEmpty() || _uiState.value.content.isBlank()
            )
        }

        return _uiState.value.contentInputError
    }

    fun generateQrCode() {
        if (isContentEmpty()) {
            return
        }

        _uiState.update { it.copy(isQrCodeGenerating = true) }
        viewModelScope.launch {
            val image = repository.createQrCode(_uiState.value.content)
            _uiState.update { it.copy(qrCodeImage = image, isQrCodeGenerated = true) }
            _uiState.update { it.copy(isQrCodeGenerating = false) }
        }
    }

    fun cancelQrCode() {
        _uiState.value = UiState()
    }

    fun saveQrCode() {
        viewModelScope.launch {
            repository.saveQrCode(
                title = _uiState.value.title.ifBlank { null },
                content = _uiState.value.content,
                image = _uiState.value.qrCodeImage!!
            )
            _uiState.value = UiState()
        }
    }
}
