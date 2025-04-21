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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainHomeViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {

    //    var doctors = mutableStateOf(emptyList<Doctor>())
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors = _doctors.asStateFlow()
    var patient = MyApp.patient

    init {
        viewModelScope.launch {
            _doctors.value = repository.getAllDoctors()
        }
    }
    fun loadDoctors() {
        viewModelScope.launch {
            _doctors.value = repository.getAllDoctors()
        }
    }

    fun updateDoctor(doctor: Doctor) {
        _doctors.update { currentList ->
            currentList.map { item ->
                if (item.id == doctor.id) doctor else item
            }
        }
    }

}