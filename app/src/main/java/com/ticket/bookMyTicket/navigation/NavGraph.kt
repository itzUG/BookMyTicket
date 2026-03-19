package com.ticket.bookMyTicket.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ticket.bookMyTicket.screens.booking.PaymentScreen
import com.ticket.bookMyTicket.screens.booking.SeatSelectionScreen
import com.ticket.bookMyTicket.screens.booking.SelectCinemaScreen
import com.ticket.bookMyTicket.screens.booking.TicketConfirmScreen
import com.ticket.bookMyTicket.screens.bottomnavigation.BottomBar
import com.ticket.bookMyTicket.screens.home.HomeScreen
import com.ticket.bookMyTicket.screens.moviedetails.MovieDetailsScreen
import com.ticket.bookMyTicket.screens.profile.ProfileScreen
import com.ticket.bookMyTicket.screens.splash.SplashScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute == Routes.HOME || currentRoute == Routes.PROFILE

    // Use a Box so the floating bottom bar overlays content rather than pushing it up
    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            // Only add bottom padding when the bar is visible so screens aren't cut off
            modifier = Modifier.fillMaxSize()
        ) {

            composable(Routes.HOME) {
                HomeScreen(navController)
            }

            composable(
                Routes.DETAILS + "{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStack ->
                val movieId = backStack.arguments?.getInt("movieId") ?: return@composable
                MovieDetailsScreen(navController, movieId)
            }

            composable(Routes.PROFILE) {
                ProfileScreen()
            }

            composable(
                Routes.CINEMA + "{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStack ->
                val movieId = backStack.arguments?.getInt("movieId") ?: return@composable
                SelectCinemaScreen(navController, movieId)
            }

            composable(
                route = "seat/{movieId}/{theatreId}/{time}/{trailerId}"
            ) { backStack ->
                val movieId   = backStack.arguments?.getString("movieId")?.toInt() ?: 0
                val theatreId = backStack.arguments?.getString("theatreId") ?: ""
                val time      = backStack.arguments?.getString("time") ?: ""
                val trailer   = backStack.arguments?.getString("trailerId") ?: ""

                SeatSelectionScreen(
                    navController = navController,
                    movieId   = movieId,
                    theatreId = theatreId,
                    showTime  = time,
                    trailerId = trailer
                )
            }

            composable(
                route = "payment/{movieId}/{theatreId}/{showTime}/{amount}/{seats}"
            ) { backStack ->
                val movieId   = backStack.arguments?.getString("movieId")?.toInt() ?: 0
                val theatreId = backStack.arguments?.getString("theatreId") ?: ""
                val showTime  = backStack.arguments?.getString("showTime") ?: ""
                val amount    = backStack.arguments?.getString("amount")?.toInt() ?: 0
                val seats     = backStack.arguments?.getString("seats") ?: ""

                PaymentScreen(
                    navController = navController,
                    amount    = amount,
                    seats     = seats.split(","),
                    movieId   = movieId,
                    theatreId = theatreId,
                    showTime  = showTime
                )
            }

            composable(
                route = "ticket_confirm/{amount}/{seats}/{movieId}/{theatreId}/{showTime}"
            ) { backStack ->
                val movieId   = backStack.arguments?.getString("movieId")?.toInt() ?: 0
                val theatreId = backStack.arguments?.getString("theatreId") ?: ""
                val showTime  = backStack.arguments?.getString("showTime") ?: ""
                val amount    = backStack.arguments?.getString("amount")?.toInt() ?: 0
                val seats     = backStack.arguments?.getString("seats") ?: ""

                TicketConfirmScreen(
                    amount    = amount,
                    seats     = seats.split(","),
                    movieId   = movieId,
                    theatreId = theatreId,
                    showTime  = showTime
                )
            }

            composable(Routes.SPLASH) {
                SplashScreen(navController)
            }
        }

        // Floating bottom bar — overlays content, only visible on Home & Profile
        if (showBottomBar) {
            BottomBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}