package com.nig.gopaddi.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.data.remote.RemoteDestination
import com.nig.gopaddi.data.remote.Trip
import com.nig.gopaddi.domain.CityResultUI
import com.nig.gopaddi.domain.usecase.TripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripUseCase: TripUseCase,
) : ViewModel() {

    var selectedCity by mutableStateOf<CityResultUI?>(null)
    var startDateMillis by mutableStateOf<Long?>(null)
    var endDateMillis by mutableStateOf<Long?>(null)
    var createTripState by mutableStateOf<Resource<Trip>?>(null)
        private set

    var destinationsState by mutableStateOf<Resource<List<RemoteDestination>>>(Resource.Loading())
        private set

    var selectedTrip by mutableStateOf<Trip?>(null)
    var searchQuery by mutableStateOf("")

    var showCreateTripSheet by mutableStateOf(false)
    var tripName by mutableStateOf("")
    var travelStyle by mutableStateOf("")
    var tripDescription by mutableStateOf("")
    val travelStyles = listOf("Solo", "Couple", "Family", "Group")
    val durationText = getTripDuration()
    var tripsState by mutableStateOf<Resource<List<Trip>>>(Resource.Loading())
        private set

    var selectedFilter by mutableStateOf("All")
    val filterOptions = listOf("All", "Planned Trips", "Solo Trips", "Group Trips", "Couple Trips")

    init {
        fetchUserTrips()
        fetchDestinations()
    }

    fun fetchUserTrips() {
        viewModelScope.launch {
            tripUseCase.getAllTrips().collect { tripsState = it }
        }
    }

    fun performCreateTrip(onSuccess: () -> Unit) {
        val newTrip = Trip(
            id = UUID.randomUUID().toString(),
            title = tripName,
            destination = selectedCity?.name ?: "Unknown",
            travelStyle = travelStyle,
            description = tripDescription,
            startDate = formatDate(startDateMillis),
            endDate = formatDate(endDateMillis),
            duration = getTripDuration(),
            imageUrl = "https://picsum.photos/seed/${tripName}/400/300"
        )

        viewModelScope.launch {
            tripUseCase.createTrip(newTrip).collect { result ->
                createTripState = result

                if (result is Resource.Success) {
                    selectedTrip = result.data ?: newTrip
                    showCreateTripSheet = false
                    resetInputs()
                    onSuccess()
                    fetchUserTrips()
                }
            }
        }
    }

    fun resetInputs() {
        selectedCity = null
        startDateMillis = null
        endDateMillis = null
        tripName = ""
        travelStyle = ""
        tripDescription = ""
    }

    val filteredUserTrips: List<Trip>
        get() {
            val allTrips = (tripsState as? Resource.Success)?.data ?: emptyList()

            return when (selectedFilter) {
                "All", "Planned Trips" -> allTrips
                else -> {
                    val styleToMatch = selectedFilter.replace(" Trips", "")
                    allTrips.filter { it.travelStyle.equals(styleToMatch, ignoreCase = true) }
                }
            }
        }

    fun canCreateTrip(): Boolean = selectedCity != null && startDateMillis != null && endDateMillis != null

    fun formatDate(millis: Long?): String {
        if (millis == null) return "Enter Date"
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("EEE, MMM d", Locale.ENGLISH))
    }

    private fun getTripDuration(): String {
        if (startDateMillis == null || endDateMillis == null) return "0 Days"
        val diff = endDateMillis!! - startDateMillis!!
        val days = (diff / (1000 * 60 * 60 * 24)).toInt()
        return "$days Days"
    }

    fun fetchDestinations() {
        viewModelScope.launch {
            tripUseCase.getDestinations().collect { destinationsState = it }
        }
    }

    val filteredDestinations: List<RemoteDestination>
        get() {
            val list = (destinationsState as? Resource.Success)?.data ?: emptyList()
            return if (searchQuery.isBlank()) list
            else list.filter {
                it.cityName.contains(searchQuery, ignoreCase = true) ||
                        it.countryName.contains(searchQuery, ignoreCase = true)
            }
        }
}
