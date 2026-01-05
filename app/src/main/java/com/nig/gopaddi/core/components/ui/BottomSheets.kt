package com.nig.gopaddi.core.components.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nig.gopaddi.R
import com.nig.gopaddi.presentation.viewmodel.TripViewModel
import com.nig.gopaddi.ui.theme.BlackColor
import com.nig.gopaddi.ui.theme.GrayTextColor
import com.nig.gopaddi.ui.theme.LightCyanGrayBgColor
import com.nig.gopaddi.ui.theme.PrimaryTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripBottomSheet(
    viewModel: TripViewModel,
    onDismiss: () -> Unit,
    onNextClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isStyleExpanded by remember { mutableStateOf(false) }

    BackHandler(enabled = true) { onDismiss() }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painterResource(R.drawable.ic_trip), null)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Create a Trip", fontSize = 14.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor)
            Text("Let's Go! Build Your Next Adventure", fontSize = 12.sp, fontWeight = FontWeight.W500, color = GrayTextColor)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Trip Name", fontSize = 14.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = viewModel.tripName,
                onValueChange = { viewModel.tripName = it },
                placeholder = { Text("Enter the trip name", color = LightCyanGrayBgColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LightCyanGrayBgColor,
                    focusedBorderColor = LightCyanGrayBgColor,
                    focusedTextColor = PrimaryTextColor,
                    cursorColor = PrimaryTextColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Travel Style", fontSize = 14.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor)
            Spacer(modifier = Modifier.height(4.dp))
            Box {
                OutlinedTextField(
                    value = viewModel.travelStyle,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select your travel style", color = LightCyanGrayBgColor) },
                    modifier = Modifier.fillMaxWidth().clickable { isStyleExpanded = true },
                    shape = RoundedCornerShape(4.dp),
                    trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null, tint = BlackColor) },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color(0xFFEAEAEA),
                        disabledTextColor = Color.Black,
                        disabledPlaceholderColor = LightCyanGrayBgColor,
                        focusedTextColor = PrimaryTextColor,
                        focusedBorderColor = LightCyanGrayBgColor,
                        cursorColor = PrimaryTextColor
                    )
                )
                Box(modifier = Modifier.matchParentSize().clickable { isStyleExpanded = true })

                DropdownMenu(
                    expanded = isStyleExpanded,
                    onDismissRequest = { isStyleExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                ) {
                    viewModel.travelStyles.forEach { style ->
                        val isSelected = viewModel.travelStyle == style
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = style,
                                        modifier = Modifier.weight(1f),
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, null, tint = Color.White)
                                    }
                                }
                            },
                            onClick = {
                                viewModel.travelStyle = style
                                isStyleExpanded = false
                            },
                            modifier = Modifier.background(
                                if (isSelected) Color(0xFF0D6EFD) else Color.Transparent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Trip Description", fontSize = 14.sp, fontWeight = FontWeight.W500, color = PrimaryTextColor)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = viewModel.tripDescription,
                onValueChange = { viewModel.tripDescription = it },
                placeholder = { Text("Tell us more about the trip", color = LightCyanGrayBgColor) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LightCyanGrayBgColor,
                    focusedBorderColor = LightCyanGrayBgColor,
                    focusedTextColor = PrimaryTextColor,
                    cursorColor = PrimaryTextColor
                    )
            )

            Spacer(modifier = Modifier.height(32.dp))

            GoPaddiBlueButton(
                text = "Next",
                onClick = {
                    if (viewModel.tripName.isNotBlank() && viewModel.travelStyle.isNotBlank()) {
                        viewModel.showCreateTripSheet = false
                        onNextClick()
                    }
                }
            )
        }
    }
}