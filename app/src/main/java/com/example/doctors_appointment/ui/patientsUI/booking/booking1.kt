package com.example.doctors_appointment.ui.patientsUI.booking

import android.content.Context
import android.widget.DatePicker
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.patientsUI.viewmodels.BookingViewModel
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.util.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

@Composable
fun BookSchedule(
    doctorId: String?,
    navController: NavController,
    bookingViewModel: BookingViewModel
) {

    if (doctorId != null) {
        bookingViewModel.getDoctorFromId(doctorId)
        bookingViewModel.fetchBookedSlotsForDoctor(doctorId, bookingViewModel.selectedDate.value)
    }

    val doctor = bookingViewModel.doctor1

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
            .padding(bottom = 70.dp)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "SCHEDULE APPOINTMENT",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = fontInria,
                color = Indigo900,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val newselectedDate = dateFormat.format(bookingViewModel.selectedDate.value)




            val selectedDate = bookingViewModel.selectedDate.value

            showDatePick(context = LocalContext.current, bookingViewModel)

            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            val selectedDateAdjusted = Calendar.getInstance().apply {
                time = selectedDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Selected Date: $newselectedDate",
                fontFamily = fontInria,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

//        val diffInMillis = selectedDateAdjusted.time - currentDate
//        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
//        val selectedTabIndex = diffInDays.coerceAtLeast(0)
            val selectedTabIndex = 3



            var selectedSlot by remember {
                mutableIntStateOf(-1)
            }
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Morning Slots:",
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Indigo900
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 0..2) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                for (i in 3..5) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                for (i in 6..8) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                for (i in 9..11) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                for (i in 12..14) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                for (i in 15..17) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))



            Text(
                text = "Evening Slots:",
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Indigo900
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 18..20) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 21..23) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 24..26) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 27..29) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 30..32) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 33..35) {
                    Slot(36 * (selectedTabIndex) + i, doctor, selectedSlot, bookingViewModel) {
                        selectedSlot = it
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            OutlinedButton(
                onClick = {
                    if (selectedSlot != -1) {
                        bookingViewModel.setDateTime(selectedSlot)
                        navController.navigate(Screen.finalBooking.route)
                    }

                }
            ) {
                Text(
                    text = "Continue",
                    fontSize = 25.sp,
                    fontFamily = fontInria,
                )
            }
        }

    }
}


@Composable
fun showDatePick(context: Context, bookingViewModel: BookingViewModel) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val selectedDate = remember { mutableStateOf(Date()) }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDate.value = selectedCalendar.time
        }, year, month, day
    )

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = "Open Date Picker")
    }

    bookingViewModel.selectedDate =  selectedDate
}

@Composable
fun Slot(
    slotNo: Int,
    doctor: Doctor,
    selectedSlot: Int,
    bookingViewModel: BookingViewModel,
    onSlotSelect: (Int) -> Unit
) {
    val appointmentTime = bookingViewModel.getAppointmentTime(slotNo % 36)
    val isBooked = bookingViewModel.bookedSlots.value.contains(appointmentTime)

    Button(
        onClick = {
            if (doctor.availabilityStatus[slotNo] && !isBooked) {
                onSlotSelect(slotNo)
            }
        },
        enabled = doctor.availabilityStatus[slotNo] && !isBooked,
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isBooked -> Color.LightGray
                slotNo == selectedSlot -> Indigo400
                else -> Color.White
            },
            contentColor = when {
                isBooked -> Color.DarkGray
                doctor.availabilityStatus[slotNo] -> {
                    if (selectedSlot == slotNo) Color.White else Indigo900
                }
                else -> Color.LightGray
            }
        )
    ) {
        val time = bookingViewModel.getTime(slotNo % 36)
        Text(
            text = String.format("%.2f", time)
        )
    }
}