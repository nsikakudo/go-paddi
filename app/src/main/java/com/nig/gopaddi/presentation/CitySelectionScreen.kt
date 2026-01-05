package com.nig.gopaddi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nig.gopaddi.R
import com.nig.gopaddi.core.util.Resource
import com.nig.gopaddi.data.remote.RemoteDestination
import com.nig.gopaddi.domain.CityResultUI
import com.nig.gopaddi.ui.theme.BackgroundColor
import com.nig.gopaddi.ui.theme.CardBgBorderColor
import com.nig.gopaddi.ui.theme.DarkGrayTextColor
import com.nig.gopaddi.ui.theme.GrayTextColor
import com.nig.gopaddi.ui.theme.LightBlueColor
import com.nig.gopaddi.ui.theme.LightCyanGrayBgColor
import com.nig.gopaddi.ui.theme.PrimaryTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(
    viewModel: TripViewModel,
    onClose: () -> Unit,
) {
    Scaffold(
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = PrimaryTextColor
                    )
                }
                Text(
                    text = "Where to?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W700,
                    color = PrimaryTextColor,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // --- Search Input ---
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        "Search for a destination...",
                        color = LightCyanGrayBgColor,
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = PrimaryTextColor,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardBgBorderColor,
                    unfocusedBorderColor = CardBgBorderColor,
                    cursorColor = PrimaryTextColor
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Remote Data Handling ---
            when (val state = viewModel.destinationsState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = LightBlueColor)
                    }
                }

                is Resource.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message ?: "An unexpected error occurred.",
                            textAlign = TextAlign.Center,
                            color = PrimaryTextColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.fetchDestinations() }) {
                            Text(
                                "Retry",
                                color = LightBlueColor,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                is Resource.Success -> {
                    val results = viewModel.filteredDestinations

                    if (results.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "The destination \"${viewModel.searchQuery}\" was not found.",
                                color = GrayTextColor,
                                fontWeight = FontWeight.W700,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            items(results) { destination ->
                                DestinationResultItem(
                                    destination = destination,
                                    onClick = {
                                        // Mapping remote data back to the UI selection
                                        viewModel.selectedCity = CityResultUI(
                                            name = "${destination.cityName}, ${destination.countryName}",
                                            airport = destination.airportName,
                                            countryCode = destination.countryCode,
                                            flagUrl = destination.countryFlag,
                                        )
                                        viewModel.searchQuery = "" // Clear search on select
                                        onClose()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationResultItem(
    destination: RemoteDestination,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mappin Icon
        Icon(
            painter = painterResource(id = R.drawable.ic_filled_mappin),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${destination.cityName}, ${destination.countryName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.W700, // Satoshi Bold
                color = PrimaryTextColor,
            )
            Text(
                text = destination.airportName,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400, // Satoshi Regular
                color = DarkGrayTextColor,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = destination.countryFlag,
                contentDescription = "Flag",
                modifier = Modifier
                    .size(24.dp, 18.dp)
                    .clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.ic_trip), // Fallback
                error = painterResource(id = R.drawable.ic_trip) // Fallback
            )
            Text(
                text = destination.countryCode,
                fontSize = 14.sp,
                color = DarkGrayTextColor,
                fontWeight = FontWeight.W500
            )
        }
    }
}


//@Composable
//fun DestinationResultItem(
//    destination: Trip,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(vertical = 16.dp, horizontal = 20.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_filled_mappin),
//            contentDescription = null,
//            modifier = Modifier.size(22.dp),
//            tint = LightBlueColor
//        )
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = destination.destination,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W700,
//                color = PrimaryTextColor,
//            )
//            Text(
//                text = destination.title,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W400,
//                color = DarkGrayTextColor,
//            )
//        }
//
////        // Optionally show the travel style as a badge
////        Surface(
////            color = CyanGrayBgColor,
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(
////                text = destination.travelStyle,
////                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
////                fontSize = 10.sp,
////                fontWeight = FontWeight.W700,
////                color = LightBlueColor
////            )
////        }
//    }
//}



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CitySelectionScreen(
//    viewModel: TripViewModel,
//    onClose: () -> Unit,
//) {
//
//    Scaffold(
//        topBar = {
//            Column {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = onClose) {
//                        Icon(Icons.Default.Close, contentDescription = "Close")
//                    }
//                    Text(
//                        text = "Where",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
//                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
//            }
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Text(
//                text = "Please select a city",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W500,
//                color = GrayTextColor,
//                modifier = Modifier.padding(horizontal = 20.dp)
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            OutlinedTextField(
//                value = viewModel.searchQuery,
//                onValueChange = { viewModel.searchQuery = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(8.dp),
//                placeholder = { Text("Search for a city...") },
////                colors = TextFieldDefaults.colors(
////                    focusedBorderColor = Color(0xFF0D6EFD), // The blue from your image
////                    unfocusedBorderColor = Color(0xFF0D6EFD),
////                    cursorColor = Color.Black
////                ),
//                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor)
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            val results = viewModel.filteredCities
//
//            if (results.isEmpty()) {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text(
//                        text = "The search word \"${viewModel.searchQuery}\" does not exist.",
//                        color = Color.Gray,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(20.dp)
//                    )
//                }
//            } else {
//                LazyColumn {
//                    items(results) { city ->
//                        CityResultItem(
//                            city = city,
//                            onClick = {
//                                viewModel.selectedCity = city
//                                viewModel.searchQuery = ""
//                                onClose()
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun CityResultItem(
//    city: CityResult,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(vertical = 16.dp, horizontal = 20.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_filled_mappin),
//            contentDescription = null,
//            modifier = Modifier.size(22.dp)
//        )
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = city.name,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W900,
//                color = PrimaryTextColor
//            )
//            Text(
//                text = city.airport,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W500,
//                color = DarkGrayTextColor
//            )
//        }
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            AsyncImage(
//                model = city.flagUrl,
//                contentDescription = null,
//                modifier = Modifier.size(24.dp, 16.dp),
//                contentScale = ContentScale.FillBounds
//            )
//            Text(
//                text = city.countryCode,
//                fontSize = 14.sp,
//                color = DarkGrayTextColor,
//                fontWeight = FontWeight.W500
//            )
//        }
//    }
//}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview3() {
//    GopaddiTheme {
//        CitySelectionScreen(
//            onClose = {},
//            viewModel = TripViewModel
//        )
//    }
//}