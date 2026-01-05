package com.nig.gopaddi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nig.gopaddi.core.components.ui.DateSelectionScreen
import com.nig.gopaddi.presentation.screens.CitySelectionScreen
import com.nig.gopaddi.presentation.screens.PlanTripScreen
import com.nig.gopaddi.presentation.screens.TripDetailsScreen
import com.nig.gopaddi.presentation.viewmodel.TripViewModel

@Composable
fun TripNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: TripViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = TripNavigationRoutes.PlanTrip.route,
        modifier = modifier
    ) {

        composable(TripNavigationRoutes.PlanTrip.route) {
            PlanTripScreen(
                viewModel = viewModel,
                onSelectCityClick = { navController.navigate(TripNavigationRoutes.SelectCity.route) },
                onSelectDateClick = { isStart ->
                    navController.navigate(TripNavigationRoutes.SelectDate.createRoute(isStart))
                },
                onNextClick = {
                    navController.navigate(TripNavigationRoutes.TripDetails.route)
                },
                onViewTripClick = {
                    navController.navigate(TripNavigationRoutes.TripDetails.route)
                }
            )
        }

        composable(TripNavigationRoutes.SelectCity.route) {
            CitySelectionScreen(
                viewModel = viewModel,
                onClose = { navController.popBackStack() },
            )
        }

        composable(
            route = TripNavigationRoutes.SelectDate.route,
            arguments = listOf(navArgument("isStartDate") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isStart = backStackEntry.arguments?.getBoolean("isStartDate") ?: true
            DateSelectionScreen(
                viewModel = viewModel,
                isSelectingStart = isStart,
                onClose = { navController.popBackStack() }
            )
        }

        composable(TripNavigationRoutes.TripDetails.route) {
            TripDetailsScreen(
                viewModel = viewModel,
                onBackClick = {
                    viewModel.selectedTrip = null
                    navController.popBackStack()
                }
            )
        }

    }
}
