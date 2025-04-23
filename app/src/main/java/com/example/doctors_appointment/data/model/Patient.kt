package com.example.doctors_appointment.data.model

import com.google.firebase.firestore.Blob


data class Patient(
    var id: String = "", // thay cho _id dạng ObjectId
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var contactNumber: String = "",
    var notification: Boolean? = null,
    var height: Double = 0.0,
    var weight: Double = 0.0,
    var gender: Boolean? = null, // true = nam, false = nữ
    var dateOfBirth: String = "", // dạng yyyy-MM-dd hoặc timestamp nếu cần
    var medicalHistory: List<Appointment> = emptyList(),
    var profileImage: Blob = Blob.fromBytes(byteArrayOf())
)