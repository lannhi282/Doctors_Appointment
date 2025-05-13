package com.example.doctors_appointment.ui.patientsUI.booking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.patientsUI.AppointmentRow
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.BookingViewModel
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel

@Composable
fun FinalBooking(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    othersViewModel: OthersViewModel
) {

    val appointment = bookingViewModel.appointment

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "APPOINTMENT\nDETAIL",
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = fontInria,
            color = Indigo900,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(40.dp))

        AppointmentRow(
            appointment,
            Modifier
                .padding(40.dp)
                .scale(1.4f),
            doctor = bookingViewModel.doctor1
        )

        Spacer(modifier = Modifier.height(35.dp))

        OutlinedButton(
            onClick = {
                bookingViewModel.onConfirm(
                    onSuccess = {
                        othersViewModel.refreshAppointments()
                        navController.navigate(Screen.appointment.route)
                    },
                    onFail = { errorMessage ->
                        Log.e("CONFIRM", errorMessage)
                    }
                )
            }
        ) {
            Text(
                text = "CONFIRM BOOKING",
                fontSize = 25.sp,
                fontFamily = fontInria,
            )
        }
    }
}