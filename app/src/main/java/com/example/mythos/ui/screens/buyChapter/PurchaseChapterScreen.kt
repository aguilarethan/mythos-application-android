package com.example.mythos.ui.screens.buyChapter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mythos.data.dtos.ChapterDto
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun PurchaseChapterScreen(
    chapterId: String,
    writerId: String,
    viewModel: PurchaseChapterViewModel,
    onBack: () -> Unit,

) {
    val chapter by viewModel.chapter.collectAsState()

    val isProcessing by viewModel.isProcessing.collectAsState()
    val purchaseMessage by viewModel.purchaseMessage.collectAsState()
    val purchaseSuccess by viewModel.purchaseSuccess.collectAsState()

    LaunchedEffect(purchaseSuccess) {
        if (purchaseSuccess) {
            onBack()
        }
    }

    LaunchedEffect(chapterId) {
        viewModel.loadChapter(chapterId)
    }

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))

    val rawDate = chapter?.createdAt // debe ser tipo String como: "2025-07-07T00:00:00"
    val formattedDate = try {
        val date = inputFormat.parse(rawDate ?: "")
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "Fecha desconocida"
    }



    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado con ícono y título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Capítulo bloqueado",
                    tint = Color(0xFFFFA000), // Amarillo
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    chapter?.let { Text(text = it.title, style = MaterialTheme.typography.titleLarge) }
                    Text(
                        text = "Capítulo ${chapter?.chapterNumber}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alerta de capítulo premium
            Surface(

                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("📌 Capítulo Premium", fontWeight = FontWeight.Bold)
                    Text("Este capítulo requiere compra para poder ser leído", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Precio + Fecha
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${chapter?.priceMythras} Mythras", fontWeight = FontWeight.Bold)
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Publicado ${formattedDate}") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(containerColor = Color.LightGray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de compra
            Button(
                onClick = { viewModel.purchaseChapter(writerId) },
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                    Text("Procesando...")
                } else {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Comprar capítulo")
                }
            }

            if (!purchaseSuccess && !purchaseMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = purchaseMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Una vez comprado, tendrás acceso permanente a este capítulo",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}