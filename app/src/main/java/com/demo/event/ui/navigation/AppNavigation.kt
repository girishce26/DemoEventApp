package com.demo.event.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demo.event.ui.detail.EventDetailScreen
import com.demo.event.ui.events.EventListScreen

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "events"
    ) {


        composable(
            route = "events"
        ) {

            EventListScreen(
                onEventClick = { eventId ->

                    navController.navigate(
                        "event/$eventId"
                    )
                }
            )
        }
        composable(
            route = "event/{eventId}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val eventId =
                backStackEntry
                    .arguments
                    ?.getString("eventId")
                    ?: return@composable

            EventDetailScreen(
                eventId = eventId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}