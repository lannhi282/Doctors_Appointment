package com.example.doctors_appointment.ui

import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl



class SignUpOp {
    suspend fun insertDoctor(doctor: Doctor) {
        FirestoreRepositoryImpl.insertDoctor(doctor)
    }

    suspend fun insertPatient(patient: Patient) {
        FirestoreRepositoryImpl.insertPatient(patient)
    }
}