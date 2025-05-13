package com.example.doctors_appointment.data.repository

import android.util.Log
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.util.Screen
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

    //    override suspend fun updateDoctor(doctor: Doctor) {
//        db.collection("doctors").document(doctor.id).set(doctor).await()
//    }
    override suspend fun updateDoctor(doctor: Doctor) {
        val docRef = if (!doctor.id.isNullOrEmpty()) {
            db.collection("doctors").document(doctor.id)
        } else {
            val newRef = db.collection("doctors").document()
            doctor.id = newRef.id
            newRef
        }

        docRef.set(doctor).await()
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

    override suspend fun auThenticateUserAsDoctor(id: String): Doctor? {
        val snapshot = db.collection("doctors")
            .whereEqualTo("id", id)
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
        return try {
            if (patientId.isEmpty()) {
                Log.w("Firestore", "Empty patientId, returning null")
                return null
            }
            Log.d("FirestoreQuery", "Querying patient with ID: $patientId")
            val document = db.collection("patients")
                .document(patientId) // Ensure a valid document ID is provided
                .get()
                .await()
            document.toObject(Patient::class.java)
        } catch (e: Exception) {
            Log.e("Firestore", "Error getting patient: ${e.message}", e)
            null
        }
    }

    override suspend fun auThenticateUserAsPatient(id: String): Patient? {
        val snapshot = db.collection("patients")
            .whereEqualTo("id", id)
            .get().await() // Đây là phương thức bất đồng bộ
        return snapshot.documents.firstOrNull()?.toObject(Patient::class.java)
    }

//    override suspend fun getAppointmentDoctorIdandDate(doctorId: String, date: Long): Appointment? {
//        return try {
//            Log.d("Firestore Query", "Searching for appointment with doctorId: $doctorId and date: $date")
//            val snapshot = db.collection("appointments")
//                .whereEqualTo("doctorId", doctorId)
//                .whereEqualTo("appointmentDate", date)
//                .get()
//                .await()
//            Log.d("Firestore Result", "Found ${snapshot.size()} appointments")
//            return snapshot.documents.firstOrNull()?.toObject(Appointment::class.java)
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error getting appointment: ${e.message}")
//            return null
//        }
//    }

    override suspend fun getAppointmentDoctorIdandDate(doctorId: String, appointmentDate: Long): Appointment? {
        return try {
            Log.d("FirestoreQuery", "Querying for doctorId: $doctorId, date: $appointmentDate")
            val snapshot = db.collection("appointments")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("appointmentDate", appointmentDate)
                .get()
                .await()

            val appointment = snapshot.documents.firstOrNull()?.toObject(Appointment::class.java)
            if (appointment == null) {
                Log.e("Firestore", "No appointment found for doctorId: $doctorId, date: $appointmentDate")
            } else {
                Log.d("Firestore", "Found appointment: ${appointment.id}, date: ${appointment.appointmentDate}")
            }
            appointment
        } catch (e: Exception) {
            Log.e("Firestore", "Error getting appointment: ${e.message}", e)
            null
        }
    }


    override suspend fun insertAppointment(appointment: Appointment) {
        val docRef = if (appointment.id.isNotEmpty()) {
            db.collection("appointments").document(appointment.id)
        } else {
            val newDocRef = db.collection("appointments").document()
            appointment.id = newDocRef.id
            newDocRef
        }

        // ✅ Format readable date
        val formattedDate = appointment.appointmentDate?.let {
            java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(it))
        } ?: "N/A"

        // ✅ Lấy tên bác sĩ
        val doctorName = appointment.doctorId?.let { id ->
            db.collection("doctors").document(id).get().await()
                .getString("name")
        } ?: "Unknown Doctor"

        // ✅ Lấy tên bệnh nhân
        val patientName = appointment.patientId?.let { id ->
            db.collection("patients").document(id).get().await()
                .getString("name")
        } ?: "Unknown Patient"

        // ✅ Tạo dữ liệu lưu Firestore
        val data = mutableMapOf<String, Any?>(
            "id" to appointment.id,
            "doctorId" to appointment.doctorId,
            "doctorName" to doctorName,
            "patientId" to appointment.patientId,
            "patientName" to patientName,
            "appointmentDate" to appointment.appointmentDate,
            "appointmentDateString" to formattedDate,
            "notes" to appointment.notes,
            "prescription" to appointment.prescription,
            "status" to appointment.status,
            "review" to appointment.review,
            "rating" to appointment.rating
        )

        docRef.set(data).await()
    }


    override suspend fun updateAppointment(appointment: Appointment) {
        if (appointment.id.isNotEmpty()) {
            try {
                db.collection("appointments")
                    .document(appointment.id)
                    .set(appointment)
                    .await()
                Log.d("Firestore", "✅ Appointment updated: ${appointment.id}")
            } catch (e: Exception) {
                Log.e("🔥 FirestoreError", "❌ Failed to update appointment ${appointment.id}: ${e.message}")
            }
        } else {
            Log.e("UPDATE", "❌ Appointment ID rỗng, không thể cập nhật")
        }
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        if (appointment.id.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("appointments")
                .document(appointment.id)
                .delete()
                .await()
        } else {
            Log.e("DELETE", "Appointment ID rỗng, không thể xoá")
        }
    }

    override suspend fun getAppointmentById(appointmentId: String): Appointment? {
        return db.collection("appointments").document(appointmentId).get().await().toObject(Appointment::class.java)
    }

    override suspend fun getAppointmentsByDoctorId(doctorId: String): List<Appointment> {
        return try {
            val snapshot = db.collection("appointments")
                .whereEqualTo("doctorId", doctorId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Appointment::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getUpcomingAppointments(userId: String, isDoctor: Boolean): List<Appointment> {
        val field = if (isDoctor) "doctorId" else "patientId"
        val now = System.currentTimeMillis()

        return try {
            val snapshot = db.collection("appointments")
                .whereEqualTo(field, userId)
                .whereGreaterThanOrEqualTo("appointmentDate", now)
                .get().await()


            if (snapshot.isEmpty) {
                Log.e(null, "🔥 No upcoming appointments found for $field = $userId, currentTime = $now")
            } else {
                println("✅ Found ${snapshot.size()} upcoming appointments for $field = $userId")
            }
            Log.d("Firestore", "Snapshot: ${snapshot.documents}") // Sửa Log.e thành Log.d, thêm tag
            snapshot.toObjects(Appointment::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to get appointments: ${e.message}", e)
            println("❌ Error fetching appointments for $field = $userId: ${e.message}")
            emptyList() // Trả về danh sách rỗng thay vì crash
        }
    }

//    override suspend fun isAppointmentSlotTaken(doctorId: String, appointmentDate: Long): Boolean {
//        return try {
//            val snapshot = db.collection("appointments")
//                .whereEqualTo("doctorId", doctorId)
//                .whereEqualTo("appointmentDate", appointmentDate)
//                .get()
//                .await()
//
//            !snapshot.isEmpty  // true nếu đã có lịch => bị trùng
//        } catch (e: Exception) {
//            Log.e("FIRESTORE", "❌ Error checking slot: ${e.message}")
//            false // giả định là không trùng nếu lỗi xảy ra, bạn có thể xử lý khác
//        }
//    }



    override suspend fun isAppointmentSlotTaken(doctorId: String, appointmentDate: Long): Boolean {
        return try {
            val snapshot = db.collection("appointments")
                .whereEqualTo("doctorId", doctorId)
                .whereGreaterThanOrEqualTo("appointmentDate", appointmentDate - 300000) // -5 minutes
                .whereLessThanOrEqualTo("appointmentDate", appointmentDate + 300000)    // +5 minutes
                .get()
                .await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FIRESTORE", "❌ Error checking slot: ${e.message}")
            false
        }
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



    override suspend fun updateDoctorRating(doctorId: String, newRating: Double) {
        db.collection("doctors")
            .document(doctorId)
            .update("rating", newRating)
            .await()
    }



}