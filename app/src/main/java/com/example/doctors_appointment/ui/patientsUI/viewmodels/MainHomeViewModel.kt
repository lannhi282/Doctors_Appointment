package com.example.doctors_appointment.ui.patientsUI.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainHomeViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {

    var doctors = mutableStateOf(emptyList<Doctor>())
    var patient = MyApp.patient

    init {
        viewModelScope.launch {
            doctors.value = repository.getAllDoctors()
        }
    }
}