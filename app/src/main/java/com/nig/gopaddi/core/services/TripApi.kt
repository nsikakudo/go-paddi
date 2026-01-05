package com.nig.gopaddi.core.services

import com.nig.gopaddi.data.remote.DestinationResponse
import com.nig.gopaddi.data.remote.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface TripApi {
    @POST("/create-trip")
    suspend fun createTrip(@Body trip: Trip): Response<Trip>

    @GET("/get-all-trips")
    suspend fun getAllTrips(): Response<List<Trip>>

    @GET("/api/destinations")
    suspend fun getDestinations(): Response<DestinationResponse>
}