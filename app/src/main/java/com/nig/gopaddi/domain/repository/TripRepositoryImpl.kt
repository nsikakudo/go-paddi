package com.nig.gopaddi.domain.repository

import com.nig.gopaddi.core.network.NetworkMonitor
import com.nig.gopaddi.core.network.safeApiCall
import com.nig.gopaddi.core.services.TripApi
import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.data.local.TripDao
import com.nig.gopaddi.data.local.TripEntity
import com.nig.gopaddi.data.remote.RemoteDestination
import com.nig.gopaddi.data.remote.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val api: TripApi,
    private val tripDao: TripDao,
    private val networkMonitor: NetworkMonitor
) : TripRepository {

    private fun TripEntity.toDomain() = Trip(id, title, destination, travelStyle, description, imageUrl, duration, startDate, endDate)
    private fun Trip.toEntity() = TripEntity(id ?: UUID.randomUUID().toString(), title, destination, travelStyle, description, imageUrl, startDate, endDate, duration ?: "")

    override suspend fun getAllTrips(): Flow<Resource<List<Trip>>> = flow {
        emit(Resource.Loading())
        val localFlow = tripDao.getAllTripsFlow()
        val count = tripDao.getTripCount()
        if (count == 0) {
            if (networkMonitor.isOnline.first()) {
                try {
                    val response = api.getAllTrips()
                    if (response.isSuccessful && response.body() != null) {
                        val remoteTrips = response.body()!!
                        tripDao.insertTrips(remoteTrips.map { it.toEntity() })
                    }
                } catch (e: Exception) { }
            }
        }

        localFlow.collect { entities ->
            emit(Resource.Success(entities.map { it.toDomain() }))
        }
    }

    override suspend fun createTrip(trip: Trip): Flow<Resource<Trip>> = flow {
        emit(Resource.Loading())
        val entity = trip.toEntity()
        tripDao.insertTrip(entity)

        if (networkMonitor.isOnline.first()) {
            safeApiCall { api.createTrip(trip) }
        }
        emit(Resource.Success(entity.toDomain()))
    }

    override suspend fun getDestinations(): Flow<Resource<List<RemoteDestination>>> = flow {
        emit(Resource.Loading())
        val result = safeApiCall { api.getDestinations().body()?.destinations ?: emptyList() }
        emit(result)
    }
}