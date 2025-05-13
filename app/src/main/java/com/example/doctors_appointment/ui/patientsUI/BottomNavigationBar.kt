package com.example.doctors_appointment.ui.patientsUI

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.CalendarViewMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.People
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.ui.DoctorNavBar
import com.example.doctors_appointment.ui.HomePage
import com.example.doctors_appointment.ui.SignIn
import com.example.doctors_appointment.ui.SignInViewModel
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.patientsUI.booking.BookSchedule
import com.example.doctors_appointment.ui.patientsUI.booking.FinalBooking
import com.example.doctors_appointment.ui.patientsUI.mainHome.MainHome
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.BookingViewModel
import com.example.doctors_appointment.ui.patientsUI.viewmodels.MainHomeViewModel
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(highNavController: NavHostController) {

    val repository: FirestoreRepository = FirestoreRepositoryImpl

    // viewModel Initialization
    val mainHomeViewModel = MainHomeViewModel(repository)
    val signInViewModel: SignInViewModel = hiltViewModel()



    val items = listOf(

        BottomNavigationItem(
            title = "Home",
            route = "mainHome",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Doctors",
            route = "doctors",
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.People,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Appoint..",
            selectedIcon = Icons.Filled.CalendarViewMonth,
            unselectedIcon = Icons.Outlined.CalendarViewMonth,
            hasNews = true,
            route = "appointment"
        ),
        BottomNavigationItem(
            title = "Profile",
            route = "profile",
            selectedIcon = Icons.Filled.Man,
            unselectedIcon = Icons.Outlined.Man,
            hasNews = true
        )
    )

    val navController = rememberNavController()
    val othersViewModel = OthersViewModel(
        repository,
        navController = navController
    )
    val bookingViewModel = BookingViewModel(repository)

    Scaffold(
        bottomBar = {
            BottomBar(navController, items)
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = Screen.mainHome.route
        ){
            composable(Screen.mainHome.route){
                MainHome(navController = navController, mainHomeViewModel = mainHomeViewModel)
            }
            composable("change_password") {
                ChangePasswordScreen(navController = navController, othersViewModel = othersViewModel)
            }
            composable(Screen.signIn.route){
                SignIn(navController = navController, signInViewModel = signInViewModel)
            }
//            composable(Screen.doctorNavBar.route){
//                DoctorNavBar(
//                    othersViewModel = othersViewModel,
////                    highNavController = highNavController
//                )
//            }

            composable(Screen.doctors.route){
                DoctorsPage(navController = navController, othersViewModel = othersViewModel)
            }

            composable(Screen.appointment.route){
                AppointmentPage(othersViewModel = othersViewModel)
            }

            composable(Screen.profile.route){
                ProfilePage(
                    navController = navController,
                    onSignOut = {
                        othersViewModel.signout()
                        highNavController.navigate(Screen.signIn.route)
                    },
                    othersViewModel = othersViewModel
                )
            }

            composable(
                route = Screen.doctorsDetails.route + "/{doctorId}",
                arguments = listOf(
                    navArgument("doctorId"){
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    }
                )
            ){ entry ->
                entry.arguments?.getString("doctorId")?.let {
                        it1 -> DoctorsDetailsPage(navController = navController, doctorId = it1, othersViewModel)
                }
            }

            composable(
                route = Screen.catagoryDoctors.route + "/{category}",
                arguments = listOf(
                    navArgument("category"){
                        type = NavType.StringType
                        defaultValue = "Heart"
                        nullable = true
                    }
                )
            ){entry ->
                CatagoryDoctorsPage(navController = navController, category =  entry.arguments?.getString("category"), othersViewModel)
            }

            composable(
                route = Screen.booking1.route + "/{doctorId}",
                arguments = listOf(
                    navArgument("doctorId"){
                        type = NavType.StringType
                        defaultValue = "Doctor1"
                        nullable = true
                    }
                )
            ){
                BookSchedule(
                    navController = navController,
                    doctorId = it.arguments?.getString("doctorId"),
                    bookingViewModel = bookingViewModel
                )
            }

            composable(
                route = Screen.finalBooking.route
            ){
                FinalBooking(navController = navController,
                    bookingViewModel = bookingViewModel,
                    othersViewModel = othersViewModel)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavController, items: List<BottomNavigationItem>) {

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(22)),
        containerColor = Indigo50,
        contentColor = Indigo900,
        tonalElevation = 5.dp,

        ){
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