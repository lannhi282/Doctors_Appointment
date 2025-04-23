package com.example.doctors_appointment.ui.DoctorUI

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date

class DoctorViewModel(
    val repository: FirestoreRepositoryImpl,
    val navController: NavController
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var user = MyApp.doctor
    var selectedDate = mutableStateOf(Date())
    val defaultPatient = Patient().apply {
        name = "John Doe"
        email = "john.doe@example.com"
        password = "password123"
        contactNumber = "1234567890" // Example contact number
        notification = true // Example notification setting
        height = 175.5 // Example height in centimeters
        weight = 70.0 // Example weight in kilograms
        gender = true // Example gender (true for male, false for female)
        dateOfBirth = "1990-01-01" // Example date of birth in yyyy-MM-dd format
        profileImage = Blob.fromBytes(byteArrayOf()) // Example path to profile image
    }
    var patientList = mutableStateOf<List<Patient>>(listOf())

    var newDoctor = Doctor().apply {
        id = user.id
        name = user.name
        email = user.email
        password = user.password
        contactNumber = user.contactNumber
        notification = user.notification
        gender = user.gender
        address = user.address
        rating = user.rating
        reviews = user.reviews // Adding all elements from user's reviews to newDoctor's reviews
        bmdcRegistrationNumber = user.bmdcRegistrationNumber
        qualifications = user.qualifications // Adding all elements from user's qualifications to newDoctor's qualifications
        about = user.about
        medicalSpecialty = user.medicalSpecialty
        profileImage = user.profileImage
        availabilityStatus = user.availabilityStatus // Adding all elements from user's availabilityStatus to newDoctor's availabilityStatus
        consultationFee = user.consultationFee
        experience = user.experience
        docoument = user.docoument // Adding all elements from user's docoument to newDoctor's docoument
        appointments = user.appointments // Adding all elements from user's appointments to newDoctor's appointments
    }


    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EditEmail -> {
                newDoctor.email = event.email
            }

            is ProfileEvent.EditGender -> {
                println("inside edit gender")
                newDoctor.gender = event.gender
                assert(newDoctor.gender == event.gender)
            }

            is ProfileEvent.EditName -> {
                newDoctor.name = event.name
            }

            is ProfileEvent.EditNumber -> {
                newDoctor.contactNumber = event.contact
            }

            is ProfileEvent.EditNotificationStatus -> {
                newDoctor.notification = event.notificationStatus
            }

            is ProfileEvent.AddQualification -> {
                newDoctor.qualifications = realmListOf(event.qualification) /// change further
            }

            is ProfileEvent.EditAbout -> {
                newDoctor.about = event.about
            }

            is ProfileEvent.EditBMDCNo -> {
                newDoctor.bmdcRegistrationNumber = event.bmdcNo
            }

            is ProfileEvent.EditExperience -> {
                newDoctor.experience = event.experience
            }

            is ProfileEvent.EditMedicalSpeciality -> {
                newDoctor.medicalSpecialty = event.medicalSpeciality
            }

            is ProfileEvent.EditAddress -> {
                newDoctor.address = event.address
            }

            is ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    repository.updateDoctor(newDoctor)
                    user = newDoctor
                }
            }


            else -> {}
        }
    }

    init {
//        val userId = auth.currentUser?.uid?:""
//        loadPatientById(userId)
    }

//Lay thông tin của bệnh nhân
//    fun loadPatient(time :) {
//        viewModelScope.launch {
//            try {
////                val result = repository.getPatientById(patientId)
////                patientState.value = result?: defaultPatient
//                val appointmentList = getAppointmentsByTime(time)
//                appointmentList.forEach { item ->
//                    val patientId = item.patientId
//                    val patient = getPatientById(patientId)
//                    patientList.map
//                }
//            } catch (e: Exception) {
//                println("Error fetching patient: ${e.message}")
//            }
//        }
//    }

    fun signout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        MyApp.doctor = Doctor() // Reset
        navController.navigate(Screen.signIn.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {  // sends flow throw the channel
        viewModelScope.launch {   // this binds the lifecycle of coroutine with our viewmodel
            _uiEvents.send(uiEvent)
        }
    }

    fun getTime(slot: Int): Double {
        return when (slot) {
            0 -> 10.00
            1 -> 10.10
            2 -> 10.20
            3 -> 10.30
            4 -> 10.40
            5 -> 10.50
            6 -> 11.00
            7 -> 11.10
            8 -> 11.20
            9 -> 11.30
            10 -> 11.40
            11 -> 11.50
            12->12.00
            13->12.10
            14->12.20
            15->12.30
            16->12.40
            17->12.50
            18->2.00
            19->2.10
            20->2.20
            21->2.30
            22->2.40
            23->2.50
            24->3.00
            25->3.10
            26->3.20
            27->3.30
            28->3.40
            29->3.50
            30->4.00
            31->4.10
            32->4.20
            33->4.30
            34->4.40
            35->4.50

            else -> -1.0
        }
    }

    fun signOut() {
        auth.signOut()
        navController.navigate(Screen.signIn.route)
    }
}