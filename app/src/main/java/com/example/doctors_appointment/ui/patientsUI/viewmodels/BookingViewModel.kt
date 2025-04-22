package com.example.doctors_appointment.ui.patientsUI.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

class BookingViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {


    var doctor1 = Doctor()
    var user = MyApp.patient
    var appointment = Appointment()
    var selectedDate = mutableStateOf(Date())
    fun getDoctorFromId(userId: String) {

        viewModelScope.launch {
            doctor1 = repository.getDoctorById(userId)!!
        }
    }

//    fun getAppointmentFromId(userId: String){
//
//        viewModelScope.launch {
//            appointment = repository.getAppointmentFromId(userId)!!
//        }
//    }

    fun setDateTime(slotNo: Int): Appointment{

        appointment.apply {
            appointment.appointmentDate = getAppointmentTime(slotNo)
        }

        return appointment
    }

    fun getAppointmentTime(slotNo: Int): Long {
        val day = slotNo / 36
        val time = getTime(slotNo % 36)

        val minutes = if (hasFraction(time)) 30 else 0

        val tomorrow = LocalDate.now().plusDays(day.toLong())
        val localTime = LocalDateTime.of(tomorrow, LocalTime.of(time.toInt(), minutes))

        return Date.from(localTime.atZone(ZoneId.systemDefault()).toInstant()).time
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

    fun hasFraction(number: Double): Boolean {
        return number != number.toInt().toDouble()
    }

    fun onConfirm() {
        viewModelScope.launch {
            repository.setAppointment(doctor1.id, user.id, appointment)
            appointment = Appointment()
        }
    }

}