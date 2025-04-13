package com.example.doctors_appointment.ui.patientsUI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Details
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.patientsUI.mainHome.RoundImage
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontActor
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo200
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel

@Composable
fun DoctorsDetailsPage(
    navController: NavController,
    doctorId: String,
    othersViewModel: OthersViewModel
) {

    othersViewModel.getDoctorFromId(doctorId)
    val doctor = othersViewModel.doctor

    Column(
        modifier = Modifier
            .background(Indigo50)
            .padding(10.dp)
    ) {
        Text(
            text = "Doctor's Profile:",
            fontSize = 20.sp,
            fontFamily = fontInria,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(50))
                .background(Indigo200)
                .padding(5.dp)
        )
        Column(
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .padding(4.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ){
            RoundImage(
                image = painterResource(id = R.drawable.man),
                modifier = Modifier.height(80.dp)
            )
            Text(
                text = "Dr. ${doctor.name}",
                fontFamily = fontInria,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "${doctor.medicalSpecialty} Specialist",
                fontFamily = fontInria,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    navController.navigate(Screen.booking1.withArgs(doctor.id))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Indigo400,
                    contentColor = Color.White,
                )
            ) {
                Text(
                    text = "Book Appointment",
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontActor,
                )
            }

        }

        val items = listOf(

            BottomNavigationItem(
                title = "About",
                route = "about",
                selectedIcon = Icons.Filled.Description,
                unselectedIcon = Icons.Outlined.Description,
                hasNews = false
            ),
            BottomNavigationItem(
                title = "Details",
                route = "details",
                selectedIcon = Icons.Filled.Details,
                unselectedIcon = Icons.Outlined.Details,
                hasNews = false
            )
        )

        var selectedTabIndex by remember {
            mutableIntStateOf(0)
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 0.dp, start = 40.dp, end = 50.dp)
                .clip(RoundedCornerShape(30))
        ) {
            items.fastForEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex ,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(text = item.title)
                    },
                    icon = {
                        Icon(
                            imageVector = if(index == selectedTabIndex)
                                item.selectedIcon else item.unselectedIcon,
                            contentDescription = null
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, bottom = 65.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{
                if(selectedTabIndex == 1){
                    DoctorsDetails(doctor)
                } else About(doctor)
            }
        }

    }


}


@Composable
fun DoctorsDetails(
    doctor: Doctor
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            //.padding(end = 15.dp, bottom = 5.dp, top = 5.dp, start = 10.dp)
            .clip(RoundedCornerShape(5))
            .border(2.dp, Indigo500, RoundedCornerShape(5))
            .background(Color.White),
        //.background(Indigo50)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, top = 12.dp, end = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {


            Text(
                text = "Address:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = doctor.address,
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Contact No:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = doctor.contactNumber,
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Email:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = doctor.email,
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
                text = if (doctor.gender == null) {
                    "Unknown"
                } else if (doctor.gender == true) {
                    "Male"
                } else "Female",
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Qualifications:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            for (item in doctor.qualifications){
                Text(
                    text = item,
                    fontSize = 16.sp,
                    fontFamily = fontInria,
                    color = Indigo900
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Experience:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = doctor.experience.toString(),
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "BMDC Registration No:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = doctor.bmdcRegistrationNumber,
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Notification:",
                fontSize = 19.sp,
                fontFamily = fontInria,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (doctor.notification == null) "UNKNOWN" else if (doctor.notification == true) "ON" else "OFF",
                fontSize = 17.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
        }
    }

}

@Composable
fun About(doctor:Doctor){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            //.padding(end = 15.dp, bottom = 5.dp, top = 5.dp, start = 10.dp)
            .clip(RoundedCornerShape(5))
            .border(2.dp, Indigo500, RoundedCornerShape(5))
            .background(Color.White)
            .padding(15.dp)
        //.background(Indigo50)
    ){
        Text(
            text = doctor.about
        )
    }
}