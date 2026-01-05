package com.nig.gopaddi.presentation.navigation

sealed class TripNavigationRoutes(val route: String) {
    data object PlanTrip : TripNavigationRoutes("plan_trip")
    data object SelectCity : TripNavigationRoutes("select_city")
    data object SelectDate : TripNavigationRoutes("select_date/{isStartDate}") {
        fun createRoute(isStartDate: Boolean) = "select_date/$isStartDate"
    }
    data object TripDetails : TripNavigationRoutes("trip_details")
}