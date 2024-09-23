package com.codingtho.qrgen.ui.screen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codingtho.qrgen.ui.screen.viewModel.GenerateScreenViewModel

@Composable
fun GenerateScreen(viewModel: GenerateScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        GenerateQrCode(viewModel)
    }

    if (uiState.isQrCodeGenerating) {
        GeneratingQrCode()
    }

    if (uiState.isQrCodeGenerated) {
        ShowGeneratedQrCodeDialog(viewModel)
    }
}

@Composable
private fun GenerateQrCode(viewModel: GenerateScreenViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            QrCodeInputs(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            QrCodeButtons(viewModel)
        }
    }
}

@Composable
private fun QrCodeInputs(viewModel: GenerateScreenViewModel) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AddTitle(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        AddContent(viewModel)
    }
}

@Composable
private fun AddTitle(viewModel: GenerateScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.title,
        onValueChange = { viewModel.onTitleChange(it) },
        label = { Text("Title") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun AddContent(viewModel: GenerateScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.content,
        onValueChange = { viewModel.onContentChange(it) },
        label = { Text("Content") },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { if (uiState.contentInputError) Text(text = "Required Field") },
        isError = uiState.contentInputError,
        singleLine = true
    )
}

@Composable
private fun QrCodeButtons(viewModel: GenerateScreenViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CancelButton(viewModel, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(0.5f))
        GenerateButton(viewModel, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CancelButton(viewModel: GenerateScreenViewModel, modifier: Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedButton(
        onClick = {
            keyboardController?.hide()
            viewModel.cancelQrCode()
        },
        modifier = modifier
    ) {
        Text("Cancel")
    }
}

@Composable
private fun GenerateButton(viewModel: GenerateScreenViewModel, modifier: Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            keyboardController?.hide()
            viewModel.generateQrCode()
        },
        modifier = modifier
    ) {
        Text("Generate")
    }
}

@Composable
private fun GeneratingQrCode() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ShowGeneratedQrCodeDialog(viewModel: GenerateScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    AlertDialog(
        onDismissRequest = { viewModel.cancelQrCode() },
        confirmButton = { SaveQrCodeButton(viewModel) },
        dismissButton = { CancelQrCodeButton(viewModel) },
        title = { Text("> QR CODE") },
        text = {
            if (uiState.qrCodeImage == null)
                Text("Something went wrong while generating the QR code.")
            else Column {
                Text("Your QR code was generated successfully.")
                Spacer(modifier = Modifier.height(16.dp))
                QrCodeImage(viewModel)
            }
        }
    )
}

@Composable
private fun QrCodeImage(viewModel: GenerateScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiState.qrCodeImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(192.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(width = 2.dp, color = Color.White)
        )
    }
}

@Composable
private fun CancelQrCodeButton(viewModel: GenerateScreenViewModel) {
    TextButton(
        onClick = { viewModel.cancelQrCode() }
    ) { Text("Cancel") }
}

@Composable
private fun SaveQrCodeButton(viewModel: GenerateScreenViewModel) {
    TextButton(
        onClick = { viewModel.saveQrCode() }
    ) { Text("Save") }
}
