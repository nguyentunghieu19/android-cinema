package com.cinema.android.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object MovieDetail : Screen("movieDetail/{movieId}") {
        fun createRoute(movieId: Int) = "movieDetail/$movieId"
    }
    data object ShowtimeSelection : Screen("showtimeSelection/{movieId}") {
        fun createRoute(movieId: Int) = "showtimeSelection/$movieId"
    }
    data object SeatSelection : Screen("seatSelection/{showtimeId}") {
        fun createRoute(showtimeId: Int) = "seatSelection/$showtimeId"
    }
    data object BookingConfirmation : Screen("bookingConfirmation/{showtimeId}/{seatIds}") {
        fun createRoute(showtimeId: Int, seatIds: List<Int>) =
            "bookingConfirmation/$showtimeId/${seatIds.joinToString("-")}"
    }

    data object Payment : Screen("payment/{bookingId}/{bookingCode}/{totalAmount}") {
        fun createRoute(bookingId: Int, bookingCode: String, totalAmount: Double) =
            "payment/$bookingId/$bookingCode/$totalAmount"
    }
    data object BookingSuccess : Screen("bookingSuccess/{bookingCode}/{totalAmount}") {
        fun createRoute(bookingCode: String, totalAmount: Double) =
            "bookingSuccess/$bookingCode/$totalAmount"
    }

    data object TicketHistory : Screen("ticketHistory")
    data object TicketDetail : Screen("ticketDetail/{bookingId}") {
        fun createRoute(bookingId: Int) = "ticketDetail/$bookingId"
    }
}