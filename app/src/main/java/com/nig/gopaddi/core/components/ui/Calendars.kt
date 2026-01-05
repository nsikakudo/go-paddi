package com.nig.gopaddi.core.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nig.gopaddi.presentation.TripViewModel
import com.nig.gopaddi.ui.theme.BackgroundColor
import com.nig.gopaddi.ui.theme.LightBlueColor
import com.nig.gopaddi.ui.theme.PrimaryTextColor
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionScreen(
    viewModel: TripViewModel,
    isSelectingStart: Boolean,
    onClose: () -> Unit
) {
    val today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (isSelectingStart) viewModel.startDateMillis else viewModel.endDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (isSelectingStart) {
                    utcTimeMillis >= today
                } else {
                    val minDate = viewModel.startDateMillis ?: today
                    utcTimeMillis >= minDate
                }
            }
        }
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Date", fontSize = 18.sp, fontWeight = FontWeight.W700, color = PrimaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = onClose) { Icon(Icons.Default.Close, "Close", tint = Color.Black) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 10.dp, color = Color.White) {
                Box(modifier = Modifier.padding(16.dp).navigationBarsPadding()) {
                    GoPaddiBlueButton(
                        text = "Choose Date",
                        onClick = {
                            if (isSelectingStart) {
                                viewModel.startDateMillis = datePickerState.selectedDateMillis
                            } else {
                                viewModel.endDateMillis = datePickerState.selectedDateMillis
                            }
                            onClose()
                        }
                    )
                }
            }
        }
    ) { padding ->
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(padding),
            showModeToggle = false,
            title = null,
            headline = null,
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                dayContentColor = Color.Black,
                disabledDayContentColor = Color.Black.copy(alpha = 0.38f),
                weekdayContentColor = Color.Black,
                selectedDayContainerColor = LightBlueColor,
                selectedDayContentColor = BackgroundColor,
                todayContentColor = LightBlueColor,
                todayDateBorderColor = LightBlueColor
            )
        )
    }
}