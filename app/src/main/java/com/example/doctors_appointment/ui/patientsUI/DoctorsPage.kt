package com.example.doctors_appointment.ui.patientsUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsPage(
    navController: NavController,
    othersViewModel: OthersViewModel
) {

    val doctors = othersViewModel.doctors
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .background(Indigo50)
                .padding(10.dp),
        ) {

            Text(
                text = "Find your Doctor",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = fontInria,
                color = Indigo900,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search doctors...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Indigo500,
                    unfocusedBorderColor = Indigo400
                )
            )
            LazyColumn(
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 65.dp)
            ){
                items(doctors.value.filter { it.name.contains(searchQuery, ignoreCase = true) }){ doctor ->
                    DoctorsRow(doctor = doctor, navController)
                }
            }
        }
    }
}

@Composable
fun DoctorsRow(
    doctor: Doctor,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(10))
            .border(2.dp, Indigo500, RoundedCornerShape(10))
            .background(Color.White)
    ){

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            Column(
                modifier = Modifier
                    .padding(start = 15.dp, top = 12.dp, end = 10.dp, bottom = 8.dp)
                    .weight(1.2f)
            ) {

                Text(
                    text = doctor.name,
                    fontSize = 15.sp,
                    fontFamily = fontInria,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = doctor.medicalSpecialty,
                    fontSize = 10.sp,
                    fontFamily = fontInria,
                    color = Indigo900
                )
                Text(
                    text = "Qualifications:",
                    fontSize = 12.sp,
                    fontFamily = fontInria,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                val qualificationText = doctor.qualifications.joinToString(", ")
                Text(
                    text = qualificationText,
                    fontSize = 8.sp,
                    fontFamily = fontInria,
                    color = Indigo900
                )

                Text(
                    text = "Experience:",
                    fontSize = 12.sp,
                    fontFamily = fontInria,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${doctor.experience}+ years",
                    fontSize = 14.sp,
                    fontFamily = fontInria,
                    color = Indigo900
                )

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(.8f)
            ) {
                Text(
                    text = "Rating:",
                    fontSize = 12.sp,
                    fontFamily = fontInria,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 5.dp),
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Indigo900,
                        modifier = Modifier.width(20.dp)
                    )

                    Text(
                        text = String.format("%.2f", doctor.rating),
                        fontSize = 15.sp,
                        fontFamily = fontInria,
                        color = Indigo900
                    )

                }

                Button(
                    onClick = {
                        navController.navigate(Screen.doctorsDetails.withArgs(doctor.id))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo400,
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Details",
                        fontFamily = fontInria,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
    }
}