package com.example.doctors_appointment.data.model


data class Doctor(
    var id: String = "", // Thay cho _id dạng ObjectId
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var contactNumber: String = "",
    var notification: Boolean? = null,
    var gender: Boolean? = null, // true for male, false for female
    var address: String = "",
    var rating: Double = 0.0,
    var reviews: List<String> = emptyList(),
    var bmdcRegistrationNumber: String = "",
    var qualifications: List<String> = emptyList(),
    var about: String = "",
    var medicalSpecialty: String = "",
    var profileImage: String = "",
    var availabilityStatus: List<Boolean> = List(6000) { true },
//    var availabilityStatus: List<Boolean> = List(100) { true },

    var consultationFee: Double = 0.0,
    var experience: Int = 0,
    var docoument: List<String> = emptyList(),
    var appointments: List<Appointment> = emptyList()
)