package com.example.doctors_appointment.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.outlined.CalendarViewMonth
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.booking.DoctorSchedule
import com.example.doctors_appointment.ui.patientsUI.BottomNavigationItem
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.DoctorUI.DoctorViewModel
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel
import com.example.doctors_appointment.ui_doctor.DoctorAppointmentDetails
import com.example.doctors_appointment.ui_doctor.PatientProfile

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorNavBar(
    highNavController: NavHostController,
//    othersViewModel: OthersViewModel
) {

    // viewModel Initialization

    val repository = FirestoreRepositoryImpl

    val signInViewModel: SignInViewModel = hiltViewModel()



    val items = listOf(


        BottomNavigationItem(
            title = "Appoint..",
            selectedIcon = Icons.Filled.CalendarViewMonth,
            unselectedIcon = Icons.Outlined.CalendarViewMonth,
            hasNews = true,
            route = Screen.doctorSchedule.route
        ),
        BottomNavigationItem(
            title = "Profile",
            route = Screen.doctorProfile.route,
            selectedIcon = Icons.Filled.Man,
            unselectedIcon = Icons.Outlined.Man,
            hasNews = true
        )
    )

    val navController = rememberNavController()
    val doctorViewModel = DoctorViewModel(repository, navController)

    Scaffold(
        bottomBar = {
            DoctorBottomBar(navController, items)
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = Screen.doctorSchedule.route
        ) {
            composable(Screen.signIn.route){
                SignIn(navController = navController, signInViewModel = signInViewModel)
            }
            composable(Screen.doctorSchedule.route) {
                DoctorSchedule(navController = navController, doctorViewModel = doctorViewModel)
            }
            composable(Screen.doctorProfile.route) {
                DoctorProfilePage(
                    onSignOut = {
                        doctorViewModel.signOut()
                        highNavController.navigate(Screen.signIn.route)
                    },
                    doctorViewModel = doctorViewModel
                )
            }

            composable(Screen.doctorAppointmentDetails.route) {
                DoctorAppointmentDetails(
                    doctorViewModel = doctorViewModel,
                    navController = navController
                )
            }

            composable(Screen.seePatientProfile.route) {
                PatientProfile(doctorViewModel = doctorViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorBottomBar(navController: NavController, items: List<BottomNavigationItem>) {

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(22)),
        containerColor = Indigo50,
        contentColor = Indigo900,
        tonalElevation = 5.dp,

        ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route)
                },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}