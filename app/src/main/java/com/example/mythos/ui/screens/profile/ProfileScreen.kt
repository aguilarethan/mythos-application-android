package com.example.mythos.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mythos.data.dtos.AccountDto
import com.example.mythos.data.managers.AccountManager

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToBecomeAuthor: () -> Unit = {},
    onNavigateToMyNovels: (String) -> Unit = {}
) {
    val currentAccount by viewModel.currentAccount.collectAsState()
    val isAccountLoaded by viewModel.isAccountLoaded.collectAsState()
    val accountError by viewModel.accountError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        accountError?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        if (!isAccountLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ProfileHeader(account = currentAccount)

            Spacer(modifier = Modifier.height(6.dp))

            ProfileMenuSection(
                viewModel = viewModel,
                account = currentAccount,
                onNavigateToBecomeAuthor = onNavigateToBecomeAuthor,
                onNavigateToMyNovels = onNavigateToMyNovels
            )

            Spacer(modifier = Modifier.height(6.dp))

            ProfileContent(account = currentAccount)
        }
    }
}

@Composable
private fun ProfileHeader(account: AccountDto?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = account?.username ?: "Usuario",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = account?.email ?: "email@ejemplo.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (account?.role != null) {
                Text(
                    text = "Rol: ${account.role}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuSection(
    viewModel: ProfileViewModel,
    account: AccountDto?,
    onNavigateToBecomeAuthor: () -> Unit,
    onNavigateToMyNovels: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(

        ) {
            // Usar el método del ViewModel para verificar si es autor
            if (viewModel.isUserAuthor()) {
                // Si es autor, mostrar solo "Mis Novelas"
                MenuOption(
                    icon = Icons.Default.Book,
                    title = "Mis novelas",
                    subtitle = "Gestiona tus obras publicadas",
                    onClick = {
                        viewModel.getCurrentUserId()?.let { userId ->
                            onNavigateToMyNovels(userId)
                        }
                    }
                )
            } else {
                // Si no es autor, mostrar "Convertirse en Autor"
                MenuOption(
                    icon = Icons.Default.Edit,
                    title = "Convertirse en autor",
                    subtitle = "Comienza a escribir y publicar",
                    onClick = onNavigateToBecomeAuthor
                )
            }
        }
    }
}

@Composable
private fun MenuOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProfileContent(account: AccountDto?) {
    // Aquí puedes agregar el resto del contenido del perfil
    // como estadísticas, configuraciones, etc.
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información de la cuenta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Agregar más información del perfil según sea necesario
            Text(
                text = "Aquí puedes agregar más información del perfil",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}