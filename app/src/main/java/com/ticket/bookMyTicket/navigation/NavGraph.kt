package com.ticket.bookMyTicket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ticket.bookMyTicket.screens.booking.SeatSelectionScreen
import com.ticket.bookMyTicket.screens.home.HomeScreen
import com.ticket.bookMyTicket.screens.moviedetails.MovieDetailsScreen
import com.ticket.bookMyTicket.screens.splash.SplashScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost (
        navController = navController,
        startDestination = Routes.HOME
    ) {
//        composable(Routes.SPLASH) {
//            SplashScreen(navController)
//        }

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.DETAILS+"{movieId}" , arguments = listOf(
            navArgument("movieId") {
                type = NavType.IntType
            }
        )) { movieId ->
            val movieId = movieId.arguments?.getInt("movieId")
            MovieDetailsScreen(navController , movieId!!)
        }


        composable(Routes.SEATS) {
            SeatSelectionScreen(navController)
        }
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }
    }

}