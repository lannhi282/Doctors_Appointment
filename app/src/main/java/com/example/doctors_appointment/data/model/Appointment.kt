package com.example.doctors_appointment.data.model

import io.realm.kotlin.ext.backlinks
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


data class Appointment(
    var id: String = "", // thay thế cho _id (ObjectId)
    var patientId: String = "", // lưu reference đến patient (Firestore không có backlinks)
    var doctorId: String = "",  // lưu reference đến doctor
    var prescription: Prescription? = null,
    var appointmentDate: Long? = null, // có thể dùng Timestamp nếu thích
    var status: String = "", // ví dụ: "pending", "completed"
    var rating: Int = 0,
    var review: String = "",
    var notes: String = ""
)