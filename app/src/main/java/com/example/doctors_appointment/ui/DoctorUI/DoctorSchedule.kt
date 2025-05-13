package com.example.doctors_appointment.ui.booking

import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.navigation.compose.rememberNavController
import com.example.doctors_appointment.MyApp.Companion.doctor
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.ui.patientsUI.BottomNavigationItem
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.DoctorUI.DoctorViewModel
import com.example.doctors_appointment.ui.patientsUI.viewmodels.BookingViewModel
import com.example.doctors_appointment.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import androidx.compose.foundation.layout.Spacer as Spacer
import androidx.compose.runtime.LaunchedEffect


@Composable
fun DoctorSchedule(
    navController: NavController,
    doctorViewModel: DoctorViewModel,
) {
    val context = LocalContext.current // Get the context for Toast

    LaunchedEffect(doctorViewModel.selectedDate.value) {
        doctorViewModel.resetSlotSelection()
        Log.d("DoctorSchedule", "Fetching booked slots for date: ${doctorViewModel.selectedDate.value}")
        doctorViewModel.fetchBookedSlotsForDoctor(
            doctorId = doctorViewModel.user.id,
            date = doctorViewModel.selectedDate.value
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
            .padding(bottom = 70.dp)
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.size(10.dp))

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
            val newselectedDate = dateFormat.format(doctorViewModel.selectedDate.value)

            showDatePick(context = LocalContext.current, doctorViewModel)

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = "Selected Date: $newselectedDate",
                fontFamily = fontInria,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            var localSelectedSlot by remember(doctorViewModel.selectedDate.value) { mutableIntStateOf(doctorViewModel.slotSelected) }

            LaunchedEffect(localSelectedSlot) {
                if (localSelectedSlot != -1) {
                    doctorViewModel.getAppointment()
                }
            }

            Text(
                text = "Morning Slots:",
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Indigo900
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Morning slots (0..17)
            for (row in 0 until 6) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (row * 3)..(row * 3 + 2)) {
                        if (i <= 17) {
                            Log.d("DoctorSchedule", "Rendering slot: $i")
                            Slot(i, doctorViewModel.user, localSelectedSlot, doctorViewModel) { slot, isBooked ->
                                localSelectedSlot = slot
                                doctorViewModel.selectSlot(slot, "onSlotSelect")
                                Log.d("DoctorSchedule", "Selected slot in callback: $slot, IsBooked: $isBooked")
                                if (isBooked) {
                                    navController.navigate(Screen.doctorAppointmentDetails.route)
                                } else {
                                    Toast.makeText(context, "Chưa có bệnh nhân nào đặt", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = "Evening Slots:",
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Indigo900
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Evening slots (18..35)
            for (row in 0 until 6) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in (18 + row * 3)..(18 + row * 3 + 2)) {
                        if (i <= 35) {
                            Log.d("DoctorSchedule", "Rendering slot: $i")
                            Slot(i, doctorViewModel.user, localSelectedSlot, doctorViewModel) { slot, isBooked ->
                                localSelectedSlot = slot
                                doctorViewModel.selectSlot(slot, "onSlotSelect")
                                Log.d("DoctorSchedule", "Selected slot in callback: $slot, IsBooked: $isBooked")
                                if (isBooked) {
                                    navController.navigate(Screen.doctorAppointmentDetails.route)
                                } else {
                                    Toast.makeText(context, "Chưa có bệnh nhân nào đặt", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun Slot(
    slotNo: Int,
    doctor: Doctor,
    selectedSlot: Int,
    doctorViewModel: DoctorViewModel,
    onSlotSelect: (Int, Boolean) -> Unit
) {
    val appointmentTime = doctorViewModel.getAppointmentTime(slotNo % 36)
    val isBooked = doctorViewModel.bookedSlots.value.contains(appointmentTime)

    Button(
        onClick = {
            if (doctor.availabilityStatus[slotNo]) {
                Log.d("Slot", "Clicked slot: $slotNo")
                doctorViewModel.selectSlot(slotNo, "onClick")
                onSlotSelect(slotNo, isBooked)
                Log.d("SlotSelection", "Selected slot: $slotNo, Time: $appointmentTime, IsBooked: $isBooked")
            }
        },
        enabled = doctor.availabilityStatus[slotNo],
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isBooked -> Indigo400
                slotNo == selectedSlot -> Indigo400
                else -> Color.White
            },
            contentColor = when {
                isBooked -> Indigo900
                doctor.availabilityStatus[slotNo] -> {
                    if (selectedSlot == slotNo) Color.White else Indigo900
                }
                else -> Color.LightGray
            }
        )
    ) {
        val time = doctorViewModel.getTime(slotNo % 36)
        Text(
            text = String.format("%.2f", time).replace(".", ":")
        )
    }
}


@Composable
fun showDatePick(context: Context, doctorViewModel: DoctorViewModel) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // ✅ Chỉ cập nhật ViewModel — phần còn lại LaunchedEffect lo
            doctorViewModel.selectedDate.value = selectedCalendar.time
        },
        year, month, day
    )

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = "Open Date Picker")
    }
}