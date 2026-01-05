package com.nig.gopaddi.domain.repository

import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.data.remote.RemoteDestination
import com.nig.gopaddi.data.remote.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    suspend fun createTrip(trip: Trip): Flow<Resource<Trip>>
    suspend fun getAllTrips(): Flow<Resource<List<Trip>>>
    suspend fun getDestinations(): Flow<Resource<List<RemoteDestination>>>
}