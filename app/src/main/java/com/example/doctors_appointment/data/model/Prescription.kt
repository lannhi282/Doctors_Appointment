package com.example.doctors_appointment.data.model


// Appointment (one to one) -> Prescription
// hence no need of id for prescription

data class Prescription(
    var problem: String = "",
    var medications: List<String> = emptyList(),
    var diagnosis: List<String> = emptyList(),
    var advice: String = ""
)