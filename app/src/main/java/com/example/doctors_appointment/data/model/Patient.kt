package com.example.doctors_appointment.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date


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
    var profileImage: String = ""
)