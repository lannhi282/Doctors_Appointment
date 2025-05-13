package com.example.doctors_appointment.ui.DoctorUI

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class DoctorViewModel(
    val repository: FirestoreRepositoryImpl,
    val navController: NavController
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var appointment = Appointment()
    var slotSelected = -1
        private set(value) { // Custom setter with backing field
            if (value in 0..35 || value == -1) { // Validate slot range or reset
                field = value // Use the backing field
                Log.d("DoctorViewModel", "Updated slotSelected to: $value from ${callerSource ?: "unknown"} at time ${System.currentTimeMillis()}")
            } else {
                Log.w("DoctorViewModel", "Invalid slotNo: $value, ignoring update")
            }
        }
    private var callerSource: String? = null // Track the source of the update

    val bookedSlots = mutableStateOf<List<Long>>(emptyList())
    var user = MyApp.doctor
    var selectedDate = mutableStateOf(Date())
    var patientSelected = Patient()
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
        reviews = user.reviews
        bmdcRegistrationNumber = user.bmdcRegistrationNumber
        qualifications = user.qualifications
        about = user.about
        medicalSpecialty = user.medicalSpecialty
        profileImage = user.profileImage
        availabilityStatus = user.availabilityStatus
        consultationFee = user.consultationFee
        experience = user.experience
        docoument = user.docoument
        appointments = user.appointments
    }

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnEvent(event: ProfileEvent) {
        // ... (existing OnEvent logic)
    }

    fun getPatient(patientId: String) {
        viewModelScope.launch {
            if (patientId.isEmpty()) {
                Log.w("DoctorViewModel", "Invalid patientId: $patientId, skipping patient fetch")
                patientSelected = Patient() // Reset or set default patient
                return@launch
            }
            Log.d("DoctorViewModel", "Fetching patient with ID: $patientId")
            val result = repository.getPatientById(patientId)
            patientSelected = result ?: Patient() // Default to empty Patient if not found
        }
    }

    fun signout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        MyApp.doctor = Doctor()
        navController.navigate(Screen.signIn.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    fun selectSlot(slotNo: Int, source: String = "unknown") {
        callerSource = source
        slotSelected = slotNo // This will use the custom setter
    }

    fun getAppointment() {
        viewModelScope.launch {
            val date = getAppointmentTime(slotSelected)
            Log.d("getAppointment", "Fetching appointment for slot: $slotSelected, Date: $date, Current slotSelected: $slotSelected, Source: getAppointment")
            appointment = repository.getAppointmentDoctorIdandDate(user.id, date) ?: Appointment()
            Log.d("getAppointment", "Doctor ID: ${user.id}, Requested date: $date")
            if (appointment.id.isEmpty()) {
                Log.w("getAppointment", "No appointment found, resetting appointment")
            } else {
                Log.d("getAppointment", "Appointment: ID=${appointment.id}, Date=${appointment.appointmentDate}, PatientID=${appointment.patientId}")
                getPatient(appointment.patientId)
            }
        }
    }

    fun fetchBookedSlotsForDoctor(doctorId: String, date: Date) {
        viewModelScope.launch {
            resetSlotSelection()
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val baseDateMillis = calendar.timeInMillis

            val slots = (0..35).map { slot ->
                val time = getTime(slot)
                val hour = time.toInt()
                val minute = ((time - hour) * 100).toInt()

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calendar.timeInMillis
            }

            val taken = mutableListOf<Long>()
            for (time in slots) {
                if (repository.isAppointmentSlotTaken(doctorId, time)) {
                    taken.add(time)
                }
            }
            bookedSlots.value = taken
            Log.d("DoctorViewModel", "Fetched booked slots: $taken")
        }
    }

    fun fetchProfileImageAsBitmap(onResult: (Bitmap?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("patients")
            .document(user.id)
            .get()
            .addOnSuccessListener { doc ->
                val blob = doc.get("profileImage") as? com.google.firebase.firestore.Blob
                val bytes = blob?.toBytes()
                if (bytes != null) {
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    onResult(bitmap)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Lỗi khi lấy ảnh nhị phân: ${it.message}")
                onResult(null)
            }
    }

    fun getAppointmentTime(slotNo: Int): Long {
        if (selectedDate.value == null) {
            Log.e("getAppointmentTime", "Selected date is null")
            return 0L
        }
        Log.d("getAppointmentTime", "Selected date: ${selectedDate.value}, Slot: $slotNo")
        val time = getTime(slotNo % 36)
        val hour = time.toInt()
        val minute = ((time - hour) * 100).toInt()

        val calendar = Calendar.getInstance()
        calendar.time = selectedDate.value
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d("getAppointmentTime", "Updated slotSelected: $slotNo, Time: ${calendar.time}, Millis: ${calendar.timeInMillis}")
        return calendar.timeInMillis
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

    fun resetSlotSelection() {
        selectSlot(-1, "resetSlotSelection")
    }

    fun signOut() {
        auth.signOut()
        navController.navigate(Screen.signIn.route)
    }
}