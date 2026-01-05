package com.nig.gopaddi.domain.usecase

import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.data.remote.RemoteDestination
import com.nig.gopaddi.data.remote.Trip
import com.nig.gopaddi.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTripUseCase @Inject constructor(private val repository: TripRepository) {
    suspend operator fun invoke(trip: Trip) = repository.createTrip(trip)
}

class GetAllTripsUseCase @Inject constructor(private val repository: TripRepository) {
    suspend operator fun invoke() = repository.getAllTrips()
}

class GetDestinationsUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<RemoteDestination>>> {
        return repository.getDestinations()
    }
}