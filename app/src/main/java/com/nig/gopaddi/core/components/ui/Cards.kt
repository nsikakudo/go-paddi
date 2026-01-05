package com.nig.gopaddi.core.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nig.gopaddi.R
import com.nig.gopaddi.core.util.TripDateParser
import com.nig.gopaddi.data.remote.Trip
import com.nig.gopaddi.ui.theme.PrimaryTextColor

@Composable
fun TripItemCard(
    trip: Trip,
    onViewClick: () -> Unit
    ) {
    val displayStartDate = TripDateParser.formatRemoteDate(trip.startDate)
    val displayDuration = TripDateParser.calculateDuration(trip.startDate, trip.endDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEAEAEA))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = trip.imageUrl ?: "https://picsum.photos/seed/${trip.id}/400/300",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.bahamas),
                    error = painterResource(R.drawable.bahamas)
                )
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = trip.destination,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = trip.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = displayStartDate, color = Color.Gray, fontSize = 13.sp)
                Text(text = displayDuration, color = Color.Gray, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            GoPaddiBlueButton(
                text = "View",
                modifier = Modifier.height(44.dp),
                onClick = onViewClick
            )
        }
    }
}



@Composable
fun PlanningBucket(
    title: String,
    description: String,
    textColor: Color,
    buttonColor: Color,
    containerColor: Color,
    buttonTextColor: Color = Color.White
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)) {
            Text(title, color = textColor, fontWeight = FontWeight.W700, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(description, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.W400)
            Spacer(Modifier.height(37.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Add $title", color = buttonTextColor)
            }
        }
    }
}



@Composable
fun ItineraryEmptyCard(title: String, icon: Int) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEAEAEA))
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painterResource(icon), null, modifier = Modifier.size(150.dp))
            Spacer(Modifier.height(8.dp))
            Text("No $title yet", fontWeight = FontWeight.W700, fontSize = 16.sp, color = PrimaryTextColor)
            Spacer(Modifier.height(12.dp))
            GoPaddiBlueButton(text = "Add $title", onClick = {}, modifier = Modifier.padding(horizontal = 49.dp))
        }
    }
}