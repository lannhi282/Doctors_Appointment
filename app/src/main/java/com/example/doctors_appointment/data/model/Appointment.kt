package com.example.doctors_appointment.data.model



data class Appointment(
    var id: String = "", // thay thế cho _id (ObjectId)
    var patientId: String = "", // lưu reference đến patient
    var doctorId: String = "",  // lưu reference đến doctor
    var doctorName: String? = null, // Thêm trường doctorName
    var patientName: String? = null, // Thêm trường patientName
    var appointmentDate: Long? = null, // Đúng kiểu Long?
    var appointmentDateString: String? = null, // Thêm trường appointmentDateString
    var prescription: Prescription? = null,
    var status: String = "", // ví dụ: "pending", "completed"
    var rating: Int = 0,
    var review: String = "",
    var notes: String = ""
)