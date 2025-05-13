package com.example.doctors_appointment.util

sealed class Screen(val route: String){
    object home: Screen("home")
    object signIn: Screen("signIn")
    object signUp : Screen("signUp")

    //    object checkUser: Screen("checkuser")
    object mainHome : Screen("mainHome")
    object doctors : Screen("doctors")
    object appointment : Screen("appointment")
    object profile : Screen("profile")
    object doctorsDetails : Screen("doctor's details page")
    object catagoryDoctors : Screen(route = "categorically doctors page")
    object booking1 : Screen(route = "book Schedule")
    object finalBooking : Screen(route = "confirm appointment")
    object doctorSchedule : Screen(route = "doctor schedule")
    object doctorNavBar : Screen(route = "doctor navigation bar")
    object doctorProfile : Screen(route = "doctor profile ")
    object doctorAppointmentDetails : Screen(route = "doctor appointment details ")
    object seePatientProfile : Screen(route = "see patient profile ")
//    object changePassword : Screen(route = "change_password")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }



}
