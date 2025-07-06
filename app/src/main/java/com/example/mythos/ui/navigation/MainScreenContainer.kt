package com.example.mythos.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mythos.ui.screens.home.HomeScreen
import com.example.mythos.ui.screens.home.HomeViewModel


/**
 * Contenedor principal para las pantallas que requieren bottom navigation
 *
 * Este composable actúa como un wrapper que:
 * 1. Proporciona la estructura Scaffold con bottom navigation
 * 2. Maneja el padding interno para que el contenido no se superponga con la barra inferior
 * 3. Determina qué pantalla mostrar basándose en la ruta actual
 *
 * @param navController El controlador de navegación
 * @param currentRoute La ruta actual para determinar qué pantalla mostrar
 */
@Composable
fun MainScreenContainer(
    navController: NavHostController,
    currentRoute: String
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                Routes.HOME -> {
                    val homeViewModel: HomeViewModel = viewModel()
                    HomeScreen(
                        viewModel = homeViewModel,
                        onNovelClick = { novelId ->
                            navController.navigate(Routes.novelWithId(novelId))
                        }
                    )
                }
                /*Routes.SEARCH -> {
                    // Aquí puedes inyectar el ViewModel cuando lo tengas
                    // val searchViewModel: SearchViewModel = viewModel()
                    SearchScreen(
                        // viewModel = searchViewModel,
                        onNovelClick = { novelId ->
                            navController.navigate(Routes.novelWithId(novelId))
                        }
                    )
                }
                Routes.PROFILE -> {
                    // Aquí puedes inyectar el ViewModel cuando lo tengas
                    // val profileViewModel: ProfileViewModel = viewModel()
                    ProfileScreen(
                        // viewModel = profileViewModel,
                        onLogout = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    )
                }*/
            }
        }
    }
}