package com.example.mythos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mythos.ui.screens.becomewriter.BecomeWriterScreen
import com.example.mythos.ui.screens.becomewriter.BecomeWriterViewModel
import com.example.mythos.ui.screens.buyChapter.PurchaseChapterScreen
import com.example.mythos.ui.screens.buyChapter.PurchaseChapterViewModel
import com.example.mythos.ui.screens.login.LoginScreen
import com.example.mythos.ui.screens.login.LoginViewModel
import com.example.mythos.ui.screens.register.RegisterScreen
import com.example.mythos.ui.screens.register.RegisterViewModel
import com.example.mythos.ui.screens.home.HomeScreen
import com.example.mythos.ui.screens.home.HomeViewModel
import com.example.mythos.ui.screens.novel.NovelScreen
import com.example.mythos.ui.screens.novel.NovelViewModel
import com.example.mythos.ui.screens.chapter.ChapterScreen
import com.example.mythos.ui.screens.chapter.ChapterViewModel


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.LOGIN, modifier = modifier) {
        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBackToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            val registerViewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable("novel/{novelId}") { backStackEntry ->
            val novelViewModel: NovelViewModel = viewModel()
            val novelId = backStackEntry.arguments?.getString("novelId") ?: return@composable
            NovelScreen(
                viewModel = novelViewModel,
                novelId = novelId,
                navController = navController,
                onChapterClick = { chapterId ->
                    navController.navigate(Routes.chapterWithId(chapterId))
                }
            )
        }

        composable("chapter/{chapterId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable
            val chapterViewModel: ChapterViewModel = viewModel()
            ChapterScreen(
                viewModel = chapterViewModel,
                chapterId = chapterId
            )
        }

        composable("purchase/{chapterId}/{writerId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable
            val writerId = backStackEntry.arguments?.getString("writerId") ?: return@composable
            val purchaseChapterViewModel: PurchaseChapterViewModel = viewModel()

            PurchaseChapterScreen(
                chapterId = chapterId,
                writerId = writerId,
                viewModel = purchaseChapterViewModel,
                onBack = {
                navController.popBackStack()
            })
        }

        composable(Routes.HOME) {
            MainScreenContainer(
                navController = navController,
                currentRoute = Routes.HOME
            )
        }

        composable(Routes.SEARCH) {
            MainScreenContainer(
                navController = navController,
                currentRoute = Routes.SEARCH
            )
        }

        composable(Routes.PROFILE) {
            MainScreenContainer(
                navController = navController,
                currentRoute = Routes.PROFILE
            )
        }

        composable(Routes.BECOME_WRITER) {
            val becomeWriterViewModel: BecomeWriterViewModel = viewModel()
            BecomeWriterScreen(
                viewModel = becomeWriterViewModel,
                onBackToProfile = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }



    }
}
