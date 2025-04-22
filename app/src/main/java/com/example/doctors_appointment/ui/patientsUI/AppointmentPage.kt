package com.example.doctors_appointment.ui.patientsUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppointmentPage(
    othersViewModel: OthersViewModel
) {
    val scope = rememberCoroutineScope()

    val items = listOf(
        BottomNavigationItem(
            title = "Upcoming",
            route = "upcoming appointments",
            selectedIcon = Icons.Filled.Upcoming,
            unselectedIcon = Icons.Outlined.Upcoming,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Past",
            route = "past appointments",
            selectedIcon = Icons.Filled.Done,
            unselectedIcon = Icons.Outlined.Done,
            hasNews = false
        )
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .background(Indigo50)
            .padding(15.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(top = 5.dp, start = 40.dp, end = 50.dp)
                .clip(RoundedCornerShape(30))
        ) {
            items.fastForEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = item.title) },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedTabIndex)
                                item.selectedIcon else item.unselectedIcon,
                            contentDescription = null
                        )
                    }
                )
            }
        }

        LazyColumn(modifier = Modifier.padding(bottom = 65.dp)) {
            val appointments =
                if (selectedTabIndex == 0) othersViewModel.upcomingAppointments.value
                else othersViewModel.pastAppointments.value

            items(appointments.size) { index ->
                val appointment = appointments[index]
                val doctor = othersViewModel.doctors.value.find { it.id == appointment.doctorId }

                doctor?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppointmentRow(
                            appointment = appointment,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            doctor = it
                        )

                        Column(
                            modifier = Modifier.wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        othersViewModel.deleteAppointment(appointment)
                                    }
                                },
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete appointment",
                                    tint = Color(0xFF0059B3)
                                )
                            }
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        val updated = appointment.copy(status = "confirmed")
                                        othersViewModel.updateAppointment(updated)
                                    }
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Autorenew,
                                    contentDescription = "Update appointment",
                                    tint = Color(0xFF0059B3)
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
fun AppointmentRow(
    appointment: Appointment,
    modifier: Modifier = Modifier,
    doctor: Doctor
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .border(2.dp, Indigo500, RoundedCornerShape(10))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 12.dp, end = 12.dp, bottom = 10.dp)
        ) {
            Text(
                text = appointment.appointmentDate?.let { convertLongToDateString(it) }
                    ?: "Time is not confirmed yet",
                fontSize = 13.sp,
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Text(
                text = "\nDoctor:",
                fontSize = 12.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = doctor.name.ifBlank { "Unknown" },
                fontSize = 10.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
            Text(
                text = "Status:",
                fontSize = 12.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = appointment.status,
                fontSize = 8.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
            Text(
                text = "Location:",
                fontSize = 12.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = doctor.address.ifBlank { "Not provided" },
                fontSize = 8.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
        }
    }
}

fun convertLongToDateString(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}