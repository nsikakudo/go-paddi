package com.nig.gopaddi.domain.usecase

import com.nig.gopaddi.data.remote.Trip
import com.nig.gopaddi.domain.repository.TripRepository
import javax.inject.Inject

class TripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend fun createTrip(trip: Trip) =
        repository.createTrip(trip)

    suspend fun getAllTrips() =
        repository.getAllTrips()

    suspend fun getDestinations() =
        repository.getDestinations()
}
