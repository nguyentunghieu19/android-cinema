package com.cinema.android.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cinema.android.ui.screen.booking.BookingConfirmationScreen
import com.cinema.android.ui.screen.booking.SeatSelectionScreen
import com.cinema.android.ui.screen.booking.ShowtimeSelectionScreen
import com.cinema.android.ui.screen.login.LoginScreen
import com.cinema.android.ui.screen.main.MainScreen
import com.cinema.android.ui.screen.movie.MovieDetailScreen
import com.cinema.android.ui.screen.payment.PaymentScreen
import com.cinema.android.ui.screen.register.RegisterScreen
import com.cinema.android.ui.screen.ticket.TicketDetailScreen
import com.cinema.android.ui.screen.ticket.TicketHistoryScreen

@Composable
fun CinemaNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            MainScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                },
                onLoggedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onViewBookingHistory = {
                    navController.navigate(Screen.TicketHistory.route)
                }
            )
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetailScreen(
                movieId = movieId,
                onBack = { navController.popBackStack() },
                onBookClick = { id ->
                    navController.navigate(Screen.ShowtimeSelection.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.ShowtimeSelection.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            ShowtimeSelectionScreen(
                movieId = movieId,
                onShowtimeSelected = { showtimeId ->
                    navController.navigate(Screen.SeatSelection.createRoute(showtimeId))
                }
            )
        }

        composable(
            route = Screen.SeatSelection.route,
            arguments = listOf(navArgument("showtimeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val showtimeId = backStackEntry.arguments?.getInt("showtimeId") ?: return@composable
            SeatSelectionScreen(
                showtimeId = showtimeId,
                onConfirm = { id, seatIds ->
                    navController.navigate(Screen.BookingConfirmation.createRoute(id, seatIds))
                }
            )
        }

        composable(
            route = Screen.BookingConfirmation.route,
            arguments = listOf(
                navArgument("showtimeId") { type = NavType.IntType },
                navArgument("seatIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val showtimeId = backStackEntry.arguments?.getInt("showtimeId") ?: return@composable
            val seatIds = backStackEntry.arguments?.getString("seatIds")
                ?.split("-")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
            BookingConfirmationScreen(
                showtimeId = showtimeId,
                seatIds = seatIds,
                onBookingSuccess = { bookingId, bookingCode, totalAmount ->
                    navController.navigate(
                        Screen.Payment.createRoute(bookingId, bookingCode, totalAmount)
                    )
                }
            )
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.IntType },
                navArgument("bookingCode") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: return@composable
            val bookingCode = backStackEntry.arguments?.getString("bookingCode") ?: ""
            val totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0

            PaymentScreen(
                bookingId = bookingId,
                onPaymentSuccess = {
                    navController.navigate(
                        Screen.BookingSuccess.createRoute(bookingCode, totalAmount)
                    ) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onPaymentFailed = { _ ->
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onCancel = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(
            route = Screen.BookingSuccess.route,
            arguments = listOf(
                navArgument("bookingCode") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val bookingCode = backStackEntry.arguments?.getString("bookingCode") ?: ""
            val totalAmount = backStackEntry.arguments?.getFloat("totalAmount") ?: 0f
            BookingSuccessScreen(
                bookingCode = bookingCode,
                totalAmount = totalAmount.toDouble(),
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TicketHistory.route) {
            TicketHistoryScreen(
                onBookingClick = { bookingId ->
                    navController.navigate(Screen.TicketDetail.createRoute(bookingId))
                }
            )
        }

        composable(
            route = Screen.TicketDetail.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: return@composable
            TicketDetailScreen(
                bookingId = bookingId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun BookingSuccessScreen(
    bookingCode: String,
    totalAmount: Double,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(text = "Dat ve thanh cong!", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Ma dat ve: $bookingCode")
        Text(text = "Tong tien: %,.0f d".format(totalAmount))
        Button(onClick = onBackToHome, modifier = Modifier.padding(top = 16.dp)) {
            Text("Ve trang chu")
        }
    }
}