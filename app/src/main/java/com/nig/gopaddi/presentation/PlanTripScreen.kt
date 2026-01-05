package com.nig.gopaddi.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nig.gopaddi.R
import com.nig.gopaddi.core.components.ui.CreateTripBottomSheet
import com.nig.gopaddi.core.components.ui.GoPaddiBlueButton
import com.nig.gopaddi.core.components.ui.InputRow
import com.nig.gopaddi.core.components.ui.ShowProgressLoader
import com.nig.gopaddi.core.components.ui.TripFilterDropdown
import com.nig.gopaddi.core.components.ui.TripItemCard
import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.ui.theme.BackgroundColor
import com.nig.gopaddi.ui.theme.DarkGrayTextColor
import com.nig.gopaddi.ui.theme.GrayTextColor
import com.nig.gopaddi.ui.theme.LightBlueColor
import com.nig.gopaddi.ui.theme.PrimaryTextColor

@Composable
fun PlanTripScreen(
    viewModel: TripViewModel,
    onSelectCityClick: () -> Unit,
    onSelectDateClick: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    onViewTripClick: () -> Unit,
) {
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.createTripState) {
        if (viewModel.createTripState is Resource.Error) {
            showErrorDialog = true
        }
    }

    Scaffold(
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Plan a Trip", fontSize = 18.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor)
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.main_bg),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Plan Your Dream Trip in Minutes", fontSize = 18.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Build, personalize, and optimize your itineraries with our trip planner. Perfect for getaways, remote workcations, and any spontaneous escapade.",
                                fontSize = 14.sp, fontWeight = FontWeight.W500, color = DarkGrayTextColor, lineHeight = 22.sp
                            )
                            Spacer(Modifier.height(20.dp))

                            Column(
                                modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp)).padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                InputRow(
                                    iconRes = R.drawable.ic_mappin, label = "Where to ?",
                                    value = viewModel.selectedCity?.name ?: "Select City",
                                    modifier = Modifier.clickable { onSelectCityClick() }
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    InputRow(
                                        iconRes = R.drawable.ic_calendar, label = "Start Date",
                                        value = viewModel.formatDate(viewModel.startDateMillis),
                                        modifier = Modifier.weight(1f).clickable { onSelectDateClick(true) }
                                    )
                                    InputRow(
                                        iconRes = R.drawable.ic_calendar, label = "End Date",
                                        value = viewModel.formatDate(viewModel.endDateMillis),
                                        modifier = Modifier.weight(1f).clickable { onSelectDateClick(false) }
                                    )
                                }
                                GoPaddiBlueButton(text = "Create a Trip", onClick = {
                                    if (viewModel.canCreateTrip()) viewModel.showCreateTripSheet = true
                                    else Toast.makeText(context, "Please complete fields", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Your Trips", fontSize = 16.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor)
                        Text(
                            "Your trip itineraries and planned trips are placed here",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = DarkGrayTextColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        when (val state = viewModel.tripsState) {
                            is Resource.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(20.dp),
                                    color = LightBlueColor
                                )
                            }

                            is Resource.Success -> {
                                TripFilterDropdown(
                                    options = viewModel.filterOptions,
                                    selected = viewModel.selectedFilter,
                                    onSelected = { viewModel.selectedFilter = it }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                val trips = viewModel.filteredUserTrips

                                if (trips.isEmpty()) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (viewModel.selectedFilter == "All")
                                                "No trips found." else "No ${viewModel.selectedFilter} found.",
                                            color = GrayTextColor,
                                            fontSize = 14.sp
                                        )
                                    }
                                } else {
                                    trips.forEach { trip ->
                                        TripItemCard(
                                            trip = trip,
                                            onViewClick = {
                                                viewModel.selectedTrip = trip
                                                onViewTripClick()
                                            }
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> {
                                Text(
                                    text = "Failed to load trips: ${state.message}",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable { viewModel.fetchUserTrips() }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(50.dp))
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                title = { Text("Trip Planning Hitch", fontWeight = FontWeight.Bold) },
                text = { Text(viewModel.createTripState?.message ?: "Something went wrong.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showErrorDialog = false
                            viewModel.performCreateTrip { onNextClick() }
                        }
                    ) {
                        Text("Retry", color = LightBlueColor, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("Cancel", color = GrayTextColor)
                    }
                }
            )
        }

        if (viewModel.showCreateTripSheet) {
            CreateTripBottomSheet(
                viewModel = viewModel,
                onDismiss = {
                    viewModel.showCreateTripSheet = false
                    viewModel.resetInputs()
                },
                onNextClick = {
                    viewModel.performCreateTrip {
                        onNextClick()
                    }
                }
            )
        }


//        if (viewModel.showCreateTripSheet) {
//            CreateTripBottomSheet(
//                viewModel = viewModel,
//                onDismiss = { viewModel.showCreateTripSheet = false },
//                onNextClick = { viewModel.performCreateTrip { onNextClick() } }
//            )
//        }

        if (viewModel.createTripState is Resource.Loading) ShowProgressLoader()
    }
}