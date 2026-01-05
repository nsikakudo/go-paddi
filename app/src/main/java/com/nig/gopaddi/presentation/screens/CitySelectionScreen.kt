package com.nig.gopaddi.presentation.screens

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
import com.nig.gopaddi.presentation.viewmodel.TripViewModel
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
                                        viewModel.selectedCity = CityResultUI(
                                            name = "${destination.cityName}, ${destination.countryName}",
                                            airport = destination.airportName,
                                            countryCode = destination.countryCode,
                                            flagUrl = destination.countryFlag,
                                        )
                                        viewModel.searchQuery = ""
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
                fontWeight = FontWeight.W700,
                color = PrimaryTextColor,
            )
            Text(
                text = destination.airportName,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
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
                placeholder = painterResource(id = R.drawable.ic_trip),
                error = painterResource(id = R.drawable.ic_trip)
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
