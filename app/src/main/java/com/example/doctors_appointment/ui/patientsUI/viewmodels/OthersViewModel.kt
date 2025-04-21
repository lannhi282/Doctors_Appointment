package com.example.doctors_appointment.ui.patientsUI.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class OthersViewModel(
    private val repository: FirestoreRepository,
    private val navController: NavController
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var doctors = mutableStateOf(emptyList<Doctor>())
    var categoryDoctors = mutableStateOf(emptyList<Doctor>())
    var doctor = Doctor()
    var upcomingAppointments = mutableStateOf(emptyList<Appointment>())
    var pastAppointments = mutableStateOf(emptyList<Appointment>())
    var user = MyApp.patient

    var newPatient: Patient = Patient().apply {
        id = user.id
        name = user.name
        email = user.email
        gender = user.gender
        contactNumber = user.contactNumber
        height = user.height
        weight = user.weight
        notification = user.notification
        medicalHistory = user.medicalHistory
        dateOfBirth = user.dateOfBirth
        profileImage = user.profileImage
        password = user.password
    }

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EditEmail -> user.email = event.email
            is ProfileEvent.EditGender -> user.gender = event.gender
            is ProfileEvent.EditName -> user.name = event.name
            is ProfileEvent.EditHeight -> user.height = event.height
            is ProfileEvent.EditWeight -> user.weight = event.weight
            is ProfileEvent.EditDoT -> user.dateOfBirth = event.dot
            is ProfileEvent.EditNumber -> user.contactNumber = event.contact
            is ProfileEvent.EditNotificationStatus -> user.notification = event.notificationStatus
//            is ProfileEvent.EditMedicalHis -> user.medicalHistory = event.medicalHis

            is ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    repository.updatePatient(user)
                    MyApp.patient = user
//                    sendUiEvent(UiEvent.ShowSnackbar("✅ Cập nhật thành công"))

                }
            }


            else -> {}
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    fun getDoctorFromId(userId: String){
        viewModelScope.launch {
            repository.getDoctorById(userId)?.let {
                doctor = it
            }
        }
    }

    fun getDoctorFromCategory(category: String){
        viewModelScope.launch {
            categoryDoctors.value = repository.getDoctorsByCategory(category)
        }
    }

    fun updatePatient(patient: Patient){
        viewModelScope.launch {
            repository.updatePatient(patient)
            MyApp.patient = patient
        }
    }

    init {
        observeDoctorsRealtime()

        viewModelScope.launch {
            doctors.value = repository.getAllDoctors()
//        }

//        viewModelScope.launch {
            pastAppointments.value = repository.getPastAppointments(user.id, isDoctor = false)
//        }

//        viewModelScope.launch {
            upcomingAppointments.value = repository.getUpcomingAppointments(user.id, isDoctor = false)
        }
    }

    suspend fun updateUpcomingAppointments() {
        viewModelScope.launch {
            upcomingAppointments.value = repository.getUpcomingAppointments(user.id, isDoctor = false)
        }
    }

    suspend fun getDoctorById(id: String): Doctor? {
        return repository.getDoctorById(id)
    }

    fun signout() {
        auth.signOut()
        navController.navigate(Screen.signIn.route)
    }

    fun observeDoctorsRealtime() {
        val db = FirebaseFirestore.getInstance()
        db.collection("doctors")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("🔥 Firestore", "Listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val updatedDoctors = snapshot.toObjects(Doctor::class.java)
                    doctors.value = updatedDoctors
                }
            }
    }

}