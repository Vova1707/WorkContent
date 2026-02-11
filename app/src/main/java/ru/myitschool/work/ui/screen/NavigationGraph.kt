package ru.myitschool.work.ui.screen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import ru.myitschool.work.ui.nav.AuthScreenDestination
import ru.myitschool.work.ui.nav.BookScreenDestination
import ru.myitschool.work.ui.nav.MainScreenDestination
import ru.myitschool.work.ui.screen.auth.AuthScreen
import ru.myitschool.work.ui.screen.main.MainScreen
import ru.myitschool.work.ui.screen.main.MainViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        navController = navController,
        startDestination = AuthScreenDestination
    ) {
        composable<AuthScreenDestination> {
            AuthScreen(navController = navController)
        }

        composable<MainScreenDestination> { backStackEntry ->
            val authCode = backStackEntry.toRoute<MainScreenDestination>().authCode

            val viewModel = remember(authCode) { MainViewModel(authCode) }
            val state by viewModel.uiState.collectAsState()

            MainScreen(
                state = state,
                onRefresh = { viewModel.onRefresh() },
                onLogout = {
                    viewModel.onLogout()
                    navController.navigate(AuthScreenDestination) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onBookClick = {
                    navController.navigate(BookScreenDestination)
                }
            )
        }

        composable<BookScreenDestination> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Экран бронирования (в разработке)")
            }
        }
    }
}