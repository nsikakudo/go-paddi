package com.nig.gopaddi.data.remote

data class Trip(
    val id: String? = null,
    val title: String,
    val destination: String,
    val travelStyle: String,
    val description: String,
    val imageUrl: String? = null,
    val duration: String? = null,
    val startDate: String,
    val endDate: String
)


data class DestinationResponse(
    val destinations: List<RemoteDestination>
)


data class RemoteDestination(
    val countryName: String,
    val cityName: String,
    val airportName: String,
    val countryFlag: String,
    val countryCode: String
)

