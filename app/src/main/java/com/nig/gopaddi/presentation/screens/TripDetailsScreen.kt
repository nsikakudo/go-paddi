package com.nig.gopaddi.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nig.gopaddi.R
import com.nig.gopaddi.core.components.ui.ActionButton
import com.nig.gopaddi.core.components.ui.ItineraryEmptyCard
import com.nig.gopaddi.core.components.ui.PlanningBucket
import com.nig.gopaddi.core.util.TripDateParser
import com.nig.gopaddi.presentation.viewmodel.TripViewModel
import com.nig.gopaddi.ui.theme.BackgroundColor
import com.nig.gopaddi.ui.theme.BlackColor
import com.nig.gopaddi.ui.theme.BorderLightGrayBgColor
import com.nig.gopaddi.ui.theme.CyanGrayBgColor
import com.nig.gopaddi.ui.theme.DarkGrayTextColor
import com.nig.gopaddi.ui.theme.DeepBlueColor
import com.nig.gopaddi.ui.theme.LightBlueColor
import com.nig.gopaddi.ui.theme.LightGrayBgColor
import com.nig.gopaddi.ui.theme.PrimaryTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    viewModel: TripViewModel,
    onBackClick: () -> Unit
) {

    val trip = viewModel.selectedTrip
    val startDate = TripDateParser.formatRemoteDate(trip?.startDate)
    val endDate = TripDateParser.formatRemoteDate(trip?.endDate)
    val duration = TripDateParser.calculateDuration(trip?.startDate, trip?.endDate)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan a Trip", fontSize = 18.sp, fontWeight = FontWeight.W700, color = BlackColor) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = BlackColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = trip?.imageUrl ?: "https://picsum.photos/seed/${trip?.id}/800/400",
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Card(
                    modifier = Modifier,
                    colors = CardDefaults.cardColors(containerColor = LightGrayBgColor),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(all =  4.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = PrimaryTextColor
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("$startDate ⟶ $endDate", fontSize = 12.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(trip?.title ?: "No Title", fontSize = 16.sp, fontWeight = FontWeight.W700, color = BlackColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${trip?.destination} | ${trip?.travelStyle}",
                    fontSize = 12.sp, fontWeight = FontWeight.W500, color = DarkGrayTextColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    ActionButton(
                        text = "Trip Collaboration",
                        icon = R.drawable.ic_collaboration,
                        modifier = Modifier.weight(1f).padding(vertical = 13.dp, horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    ActionButton(
                        text = "Share Trip",
                        icon = R.drawable.ic_share,
                        modifier = Modifier.weight(1f).padding(vertical = 13.dp, horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                PlanningBucket(
                    title = "Activities",
                    description = "Build, personalize, and optimize your itineraries with our trip planner.",
                    buttonColor = LightBlueColor,
                    buttonTextColor = BackgroundColor,
                    containerColor = DeepBlueColor,
                    textColor = BackgroundColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                PlanningBucket(
                    title = "Hotels",
                    description = "Build, personalize, and optimize your itineraries with our trip planner.",
                    buttonColor = LightBlueColor,
                    buttonTextColor = BackgroundColor,
                    containerColor = CyanGrayBgColor,
                    textColor = PrimaryTextColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                PlanningBucket(
                    title = "Flights",
                    description = "Build, personalize, and optimize your itineraries with our trip planner.",
                    buttonColor = BackgroundColor,
                    buttonTextColor = LightBlueColor,
                    containerColor = LightBlueColor,
                    textColor = BackgroundColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Trip Itinerary", fontSize = 16.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor)

                Text("Your trip itineraries are placed here", fontSize = 12.sp, fontWeight = FontWeight.W500, color = DarkGrayTextColor)

                Spacer(modifier = Modifier.height(25.dp))

                ItineraryEmptyCard(
                    title = "Flights",
                    headerIcon = R.drawable.ic_flights_illustrator,
                    illustration = R.drawable.ic_activity,
                    containerColor = BorderLightGrayBgColor,
                    contentTextColor = PrimaryTextColor,
                    contentColor = PrimaryTextColor
                )

                ItineraryEmptyCard(
                    title = "Hotels",
                    headerIcon = R.drawable.ic_hotel_illustrator,
                    illustration = R.drawable.ic_hotel,
                    containerColor = Color(0xFF344054),
                    contentTextColor = BackgroundColor,
                )

                ItineraryEmptyCard(
                    title = "Activities",
                    headerIcon = R.drawable.ic_activities_illustrator,
                    illustration = R.drawable.ic_flight,
                    containerColor = LightBlueColor,
                    contentTextColor = BackgroundColor,
                )
            }
        }
    }
}