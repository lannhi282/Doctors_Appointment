package com.example.doctors_appointment.ui_doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.model.Prescription
import com.example.doctors_appointment.ui.patientsUI.AppointmentView
//import com.example.doctors_appointment.ui.patientsUI.AppointmentView
import com.example.doctors_appointment.ui.patientsUI.mainHome.RoundImage
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.DoctorUI.DoctorViewModel
import com.example.doctors_appointment.ui.patientsUI.AppointmentRow
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.util.Screen

@Composable
fun DoctorAppointmentDetails(
    navController: NavController,
    doctorViewModel: DoctorViewModel,
) {

    val appointment = Appointment().apply {

        // Assign values to appointment properties
        prescription = Prescription() // Assuming you have a Prescription class
        appointmentDate =
            System.currentTimeMillis() // Example appointment date as current time in milliseconds
        status = "Scheduled" // Example status
        rating = 0 // Example initial rating
        review = "" // Empty review initially
        notes = "Follow up in 2 weeks" // Example appointment notes
    }

    val patient = Patient().apply {
        name = "Md. Minhajul Haque"
        email = "minhajul331047@gmail.com"
        password = "password123"
        contactNumber = "1234567890" // Example contact number
        notification = true // Example notification setting
        height = 175.5 // Example height in centimeters
        weight = 70.0 // Example weight in kilograms
        gender = true // Example gender (true for male, false for female)
        dateOfBirth = "1990-01-01" // Example date of birth in yyyy-MM-dd format
        profileImage = "path_to_image.jpg" // Example path to profile image
    }



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
            doctorViewModel.user
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate(Screen.seePatientProfile.route)
                //navController.navigate(Screen.doctorsDetails.withArgs(patient._id.toHexString()))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Indigo400,
                contentColor = Color.White,
            )
        ) {
            Text(
                text = "Patient's Profile",
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

    }
}

@Composable
fun PatientProfile(
    doctorViewModel: DoctorViewModel
) {

    val patient = Patient().apply {
        name = "lam"
        email = "lam@gmail.com"
        password = "lamlt123@"
        contactNumber = "1234567890" // Example contact number
        notification = true // Example notification setting
        height = 175.0 // Example height in centimeters
        weight = 56.0 // Example weight in kilograms
        gender = true // Example gender (true for male, false for female)
        dateOfBirth = "2005-11-18" // Example date of birth in yyyy-MM-dd format
        profileImage = "path_to_image.jpg" // Example path to profile image
    }



    Column(
        modifier = Modifier
            .background(Indigo50)
            .padding(10.dp),
    ) {

        Column(
            modifier = Modifier
                .height(155.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            RoundImage(
                image = painterResource(id = R.drawable.man),
                modifier = Modifier.height(80.dp)
            )
            Text(
                text = patient.name,
                fontFamily = fontInria,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 65.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                Profile(patient)

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Medical History:",
                    fontSize = 25.sp,
                    fontFamily = fontInria,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(7.dp))

                for (item in patient.medicalHistory) {
                    AppointmentView(item)
                }

            }
        }
    }


}


@Composable
fun Profile(
    patient: Patient
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5))
            .border(2.dp, Indigo500, RoundedCornerShape(5))
            .background(Color.White)

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, top = 12.dp, end = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Height",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = patient.height.toString(),
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Weight",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = patient.weight.toString(),
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Gender",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (patient.gender == null) {
                    "Unknown"
                } else if (patient.gender == true) {
                    "Male"
                } else "Female",
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Date of Birth:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = patient.dateOfBirth,
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )


            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Phone:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = patient.contactNumber,
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}