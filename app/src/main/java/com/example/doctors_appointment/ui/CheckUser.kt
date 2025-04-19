package com.example.doctors_appointment.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.ui.patientsUI.BottomNavigationItem
import com.example.doctors_appointment.ui.patientsUI.NavBar
import com.example.doctors_appointment.util.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val auth = FirebaseAuth.getInstance()
// Chuyển CheckUser thành suspend function để sử dụng với coroutine
suspend fun CheckUser(

): Boolean {
    val repository = FirestoreRepositoryImpl

    // Sử dụng withContext để chuyển qua dispatcher phù hợp với Firebase
    val doctor = withContext(Dispatchers.IO) { repository.auThenticateUserAsDoctor(auth.currentUser?.uid ?: "") }
    val patient = withContext(Dispatchers.IO) { repository.auThenticateUserAsPatient(auth.currentUser?.uid ?: "") }

    if (doctor != null) {
        MyApp.doctor = doctor
        println("doctor matched")
        return false
    } else if (patient != null) {
        MyApp.patient = patient
        println("patient matched")
        return true
    } else {
        println("No match found")
        return false
    }
}