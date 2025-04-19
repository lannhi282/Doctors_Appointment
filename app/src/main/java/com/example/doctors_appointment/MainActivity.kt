package com.example.doctors_appointment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.CheckUser
import com.example.doctors_appointment.ui.DoctorNavBar
import com.example.doctors_appointment.ui.HomePage
import com.example.doctors_appointment.ui.SignIn
import com.example.doctors_appointment.ui.SignUp
import com.example.doctors_appointment.ui.SignInViewModel
import com.example.doctors_appointment.ui.SignUpViewModel
import com.example.doctors_appointment.ui.patientsUI.NavBar
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val signInViewModel: SignInViewModel = hiltViewModel()
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val navController = rememberNavController()


            NavHost(
                navController = navController,
                startDestination = Screen.home.route
            ){
                composable(Screen.home.route){
                    HomePage(navController = navController)
                }

                composable(Screen.signIn.route) {
                    SignIn(navController = navController, signInViewModel = signInViewModel)
                }

                composable(Screen.signUp.route) {
                    SignUp(navController = navController, signUpViewModel = signUpViewModel)
                }

                composable(Screen.mainHome.route) {
                    NavBar()
                }

                composable(Screen.doctorNavBar.route) {
                    DoctorNavBar()
                }
//
//                composable(Screen.profile.route){
//                    ProfilePage(navController = navController)
//                }
            }
        }
    }
}