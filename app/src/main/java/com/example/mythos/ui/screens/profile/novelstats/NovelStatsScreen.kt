package com.example.mythos.ui.screens.profile.novelstats

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mythos.data.dtos.NovelStatsDto
import com.example.mythos.data.managers.AccountManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NovelStatsScreen(
    viewModel: NovelStatsViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var stats by remember { mutableStateOf<List<NovelStatsDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var expandedNovelId by remember { mutableStateOf<String?>(null) }

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Contenido superior con fechas
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showDatePicker { startDate = it } },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (startDate.isEmpty()) "Fecha Inicio" else startDate)
                }
                Button(
                    onClick = { showDatePicker { endDate = it } },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (endDate.isEmpty()) "Fecha Fin" else endDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Lista de estadísticas
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {
            items(stats) { novel ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        expandedNovelId = if (expandedNovelId == novel.novelId) null else novel.novelId
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(novel.coverImageUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = novel.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = "Mythras",
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${novel.totalMythras}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Icon(
                                imageVector = if (expandedNovelId == novel.novelId)
                                    Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (expandedNovelId == novel.novelId)
                                    "Contraer" else "Expandir"
                            )
                        }

                        if (expandedNovelId == novel.novelId) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Estadísticas por capítulo:",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            novel.chapters.forEach { chapter ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Capítulo ${chapter.chapterNumber}: ${chapter.title}",
                                            fontWeight = FontWeight.Medium,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Compras",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = "${chapter.totalPurchases}",
                                                    fontWeight = FontWeight.Medium,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "Precio",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.MonetizationOn,
                                                        contentDescription = "Mythras",
                                                        modifier = Modifier.size(16.dp),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${chapter.priceMythras}",
                                                        fontWeight = FontWeight.Medium,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }

                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = "Total",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.MonetizationOn,
                                                        contentDescription = "Mythras",
                                                        modifier = Modifier.size(16.dp),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${chapter.totalMythras}",
                                                        fontWeight = FontWeight.Medium,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        stats = viewModel.getAuthorNovelStats(
                            AccountManager.getCurrentUserId().toString(),
                            startDate,
                            endDate
                        )
                        error = null
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Buscar estadísticas")
        }
    }
}
