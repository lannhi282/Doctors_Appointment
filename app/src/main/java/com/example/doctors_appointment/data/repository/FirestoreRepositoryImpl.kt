package com.example.doctors_appointment.data.repository

import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


object FirestoreRepositoryImpl : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun insertDoctor(doctor: Doctor) {
        if (doctor.id.isEmpty()) {
            doctor.id = db.collection("doctors").document().id
        }

        db.collection("doctors").document(doctor.id).set(doctor).await()
    }

    override suspend fun deleteDoctor(doctorId: String) {
        db.collection("doctors").document(doctorId).delete().await()
    }

    override suspend fun updateDoctor(doctor: Doctor) {
        db.collection("doctors").document(doctor.id).set(doctor).await()
    }

    override suspend fun getAllDoctors(): List<Doctor> {
        return db.collection("doctors").get().await().toObjects(Doctor::class.java)
    }

    override suspend fun getDoctorById(doctorId: String): Doctor? {
        return db.collection("doctors").document(doctorId).get().await().toObject(Doctor::class.java)
    }

    override suspend fun getDoctorsByCategory(category: String): List<Doctor> {
        return db.collection("doctors")
            .whereEqualTo("medicalSpecialty", category)
            .get().await().toObjects(Doctor::class.java)
    }

    override suspend fun auThenticateUserAsDoctor(email: String, password: String): Doctor? {
        val snapshot = db.collection("doctors")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().await() // Đây là phương thức bất đồng bộ
        return snapshot.documents.firstOrNull()?.toObject(Doctor::class.java)
    }

    override suspend fun insertPatient(patient: Patient) {
        // Kiểm tra nếu patient.id trống, tạo ID mới nếu cần
        val patientRef = if (patient.id.isNotEmpty()) {
            db.collection("patients").document(patient.id)
        } else {
            db.collection("patients").document()  // Firebase tự tạo ID
        }

        patientRef.set(patient).await()
    }

    override suspend fun updatePatient(patient: Patient) {
        val docRef = if (!patient.id.isNullOrEmpty()) {
            db.collection("patients").document(patient.id)
        } else {
            val newRef = db.collection("patients").document()
            patient.id = newRef.id
            newRef
        }

        docRef.set(patient).await()
    }

    override suspend fun deletePatient(patientId: String) {
        db.collection("patients").document(patientId).delete().await()
    }

    override suspend fun getPatientById(patientId: String): Patient? {
        return db.collection("patients").document(patientId).get().await().toObject(Patient::class.java)
    }

    override suspend fun auThenticateUserAsPatient(email: String, password: String): Patient? {
        val snapshot = db.collection("patients")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().await() // Đây là phương thức bất đồng bộ
        return snapshot.documents.firstOrNull()?.toObject(Patient::class.java)
    }

    override suspend fun insertAppointment(appointment: Appointment) {
        val docRef = if (appointment.id.isNotEmpty()) {
            db.collection("appointments").document(appointment.id)
        } else {
            // Tạo mới ID nếu chưa có, đồng thời gán lại vào appointment
            val newDocRef = db.collection("appointments").document()
            appointment.id = newDocRef.id
            newDocRef
        }

        docRef.set(appointment).await()
    }
    override suspend fun updateAppointment(appointment: Appointment) {
        db.collection("appointments").document(appointment.id).set(appointment).await()
    }

    override suspend fun deleteAppointment(appointmentId: String) {
        db.collection("appointments").document(appointmentId).delete().await()
    }

    override suspend fun getAppointmentById(appointmentId: String): Appointment? {
        return db.collection("appointments").document(appointmentId).get().await().toObject(Appointment::class.java)
    }

    override suspend fun getUpcomingAppointments(userId: String, isDoctor: Boolean): List<Appointment> {
        val field = if (isDoctor) "doctorId" else "patientId"
        val now = System.currentTimeMillis()

        val snapshot = db.collection("appointments")
            .whereEqualTo(field, userId)
            .whereGreaterThanOrEqualTo("appointmentDate", now)
            .get().await()

        if (snapshot.isEmpty) {
            // In log nếu không có kết quả nào
            println("🔥 No upcoming appointments found for $field = $userId, currentTime = $now")
        } else {
            println("✅ Found ${snapshot.size()} upcoming appointments for $field = $userId")
        }

        return snapshot.toObjects(Appointment::class.java)
    }

    override suspend fun getPastAppointments(userId: String, isDoctor: Boolean): List<Appointment> {
        val field = if (isDoctor) "doctorId" else "patientId"
        val now = System.currentTimeMillis()
        return db.collection("appointments")
            .whereEqualTo(field, userId)
            .whereLessThan("appointmentDate", now)
            .get().await().toObjects(Appointment::class.java)
    }

    override suspend fun setAppointment(doctorId: String, patientId: String, appointment: Appointment) {
        insertAppointment(appointment.copy(doctorId = doctorId, patientId = patientId))
    }

}
