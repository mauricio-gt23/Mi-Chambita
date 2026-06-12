package com.michambita.feature.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michambita.feature.profile.components.LogoutConfirmDialog
import com.michambita.feature.profile.components.ProfileHeader
import com.michambita.feature.profile.components.ProfileInfoCard
import com.michambita.feature.profile.components.ProfileInfoItem
import com.michambita.feature.profile.viewmodel.ProfileViewModel
import com.michambita.ui.components.widget.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.logoutEvent.collect {
            onLogout()
        }
    }

    when {
        uiState.isLoading -> {
            LoadingOverlay(
                message = "Cargando perfil...",
            )
        }

        uiState.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState.error ?: "Error desconocido",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        uiState.user != null -> {
            val user = uiState.user!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProfileHeader(
                    name = user.name.orEmpty(),
                    email = user.email.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileInfoCard(
                    title = "Información personal",
                    items = listOf(
                        ProfileInfoItem(
                            icon = Icons.Default.Person,
                            label = "Nombre",
                            value = user.name.orEmpty().ifEmpty { "Sin nombre" },
                        ),
                        ProfileInfoItem(
                            icon = Icons.Default.Email,
                            label = "Correo electrónico",
                            value = user.email.orEmpty().ifEmpty { "Sin correo" },
                        ),
                        ProfileInfoItem(
                            icon = Icons.Default.Badge,
                            label = "Rol",
                            value = if (user.ctrlAdmin) "Administrador" else "Colaborador",
                        ),
                    ),
                )

                uiState.empresa?.let { empresa ->
                    ProfileInfoCard(
                        title = "Mi empresa",
                        items = listOfNotNull(
                            ProfileInfoItem(
                                icon = Icons.Default.Business,
                                label = "Nombre de empresa",
                                value = empresa.nombre,
                            ),
                            empresa.descripcion?.takeIf { it.isNotBlank() }?.let {
                                ProfileInfoItem(
                                    icon = Icons.Default.Category,
                                    label = "Descripción",
                                    value = it,
                                )
                            },
                            empresa.businessType?.let {
                                ProfileInfoItem(
                                    icon = Icons.Default.Category,
                                    label = "Tipo de negocio",
                                    value = it.name,
                                )
                            },
                        ),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = viewModel::showLogoutDialog,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoggingOut,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text(text = "Cerrar sesión")
                }

                if (uiState.isLoggingOut) {
                    LoadingOverlay(
                        modifier = Modifier,
                        message = "Cerrando sesión...",
                    )
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = viewModel::onLogoutConfirmed,
            onDismiss = viewModel::dismissLogoutDialog,
        )
    }
}
