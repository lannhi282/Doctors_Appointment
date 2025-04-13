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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun CatagoryDoctorsPage(
    navController: NavController,
    category: String?,
    othersViewModel: OthersViewModel
) {

    if (category != null) {
        othersViewModel.getDoctorFromCategory(category)
    }else{
        navController.popBackStack()
    }

    val categoryDoctors = othersViewModel.categoryDoctors.value


    Box(
        modifier = Modifier
            .fillMaxSize()
    ){

        Column(
            modifier = Modifier
                .background(Indigo50)
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = fontInria,
                color = Indigo900,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(categoryDoctors.size){
                    CategoryDoctorsRow(doctor = categoryDoctors[it], navController)
                }
            }
        }
    }

}


@Composable
fun CategoryDoctorsRow(
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
        //.background(Indigo50)

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
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontInria,
                    )
                }

            }

        }
    }
}