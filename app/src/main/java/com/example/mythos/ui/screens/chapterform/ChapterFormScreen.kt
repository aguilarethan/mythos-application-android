package com.example.mythos.ui.screens.chapterform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterFormScreen(
    viewModel: ChapterFormViewModel = viewModel(),
    onChapterSaved: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val chapterForm by viewModel.chapterForm.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BasicTextField(
            value = chapterForm.title,
            onValueChange = viewModel::onTitleChange,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            decorationBox = { innerTextField ->
                if (chapterForm.title.isEmpty()) {
                    Text(
                        "Título del capítulo...",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.Gray)
                    )
                }
                innerTextField()
            }
        )

        BasicTextField(
            value = chapterForm.content,
            onValueChange = viewModel::onContentChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 24.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
                .padding(bottom = 24.dp),
            decorationBox = { innerTextField ->
                if (chapterForm.content.isEmpty()) {
                    Text(
                        "Escribe tu capítulo aquí...",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                }
                innerTextField()
            }
        )

        // Precio: lo mantenemos estilizado pero minimal
        OutlinedTextField(
            value = chapterForm.priceMythras.toString(),
            onValueChange = {
                val newPrice = it.toIntOrNull() ?: 0
                viewModel.onPriceChange(newPrice)
            },
            label = { Text("Precio en Mythras") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Botones con espacio aireado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onNavigateBack) {
                Text("Cancelar")
            }
            Button(
                onClick = { viewModel.saveChapter(onChapterSaved) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar")
            }
        }
    }
}
