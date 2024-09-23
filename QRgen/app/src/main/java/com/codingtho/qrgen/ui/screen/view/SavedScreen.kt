package com.codingtho.qrgen.ui.screen.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.codingtho.qrgen.data.repository.model.QrCode
import com.codingtho.qrgen.ui.screen.viewModel.SavedScreenViewModel

@Composable
fun SavedScreen(viewModel: SavedScreenViewModel = hiltViewModel()) {
    val qrCodes by viewModel.qrCodes.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (qrCodes.isEmpty())
            Text(
                text = "No QR codes saved",
                modifier = Modifier.align(Alignment.Center),
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        else SavedQrCodesList(viewModel)
    }
}

@Composable
private fun SavedQrCodesList(viewModel: SavedScreenViewModel) {
    val qrCodes by viewModel.qrCodes.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(qrCodes) {
            QrCodeItem(viewModel, it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QrCodeItem(viewModel: SavedScreenViewModel, qrCode: QrCode) {
    val showInfoDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = { showInfoDialog.value = true },
                onLongClick = { showDeleteDialog.value = true }
            )
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            QrCodeImage(qrCode.image)
            QrCodeTitle(qrCode.title)
        }
    }

    if (showInfoDialog.value) QrCodeInfo(qrCode, showInfoDialog)
    if (showDeleteDialog.value) QrCodeDelete(viewModel, qrCode, showDeleteDialog)
}

@Composable
private fun QrCodeInfo(qrCode: QrCode, showInfoDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showInfoDialog.value = false },
        confirmButton = {
            TextButton(
                onClick = { showInfoDialog.value = false }
            ) {
                Text("Close")
            }
        },
        title = { Text("> INFO") },
        text = {
            Column {
                QrCodeImage(qrCode.image, size = 240.dp)
                Spacer(modifier = Modifier.height(16.dp))
                QrCodeTitle(qrCode.title)
                QrCodeContent(qrCode.content)
            }
        }
    )
}

@Composable
private fun QrCodeDelete(
    viewModel: SavedScreenViewModel,
    qrCode: QrCode,
    showDeleteDialog: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteQrCode(qrCode)
                    showDeleteDialog.value = false
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showDeleteDialog.value = false }
            ) {
                Text("Cancel")
            }
        },
        title = { Text("> DELETE") },
        text = {
            Text("Are you sure you want to delete this QR code?")
        }
    )
}

@Composable
private fun QrCodeImage(image: String, size: Dp = 128.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(4.dp))
                .border(width = 2.dp, color = Color.White)
        )
    }
}

@Composable
private fun QrCodeTitle(title: String?) {
    Text(
        text = title ?: "Untitled",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.Bold,
        maxLines = 1
    )
}

@Composable
private fun QrCodeContent(content: String) {
    Text(
        text = content,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
    )
}
