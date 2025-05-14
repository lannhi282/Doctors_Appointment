package com.example.doctors_appointment.ui.patientsUI.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.services.MailSender
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class BookingViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {

    val bookedSlots = mutableStateOf<List<Long>>(emptyList())
    var doctor1 = Doctor()
    var user = MyApp.patient
    var appointment = Appointment()
    var selectedDate = mutableStateOf(Date())

    fun getDoctorFromId(userId: String) {
        viewModelScope.launch {
            doctor1 = repository.getDoctorById(userId)!!
        }
    }

    fun setDateTime(slotNo: Int): Appointment {
        appointment.apply {
            appointmentDate = getAppointmentTime(slotNo)
        }
        return appointment
    }

    fun getAppointmentTime(slotNo: Int): Long {
        val time = getTime(slotNo % 36)
        val hour = time.toInt()
        val minute = ((time - hour) * 100).toInt()

        val calendar = Calendar.getInstance()
        calendar.time = selectedDate.value
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    fun getTime(slot: Int): Double {
        return when (slot) {
            0 -> 10.00; 1 -> 10.10; 2 -> 10.20; 3 -> 10.30; 4 -> 10.40; 5 -> 10.50
            6 -> 11.00; 7 -> 11.10; 8 -> 11.20; 9 -> 11.30; 10 -> 11.40; 11 -> 11.50
            12 -> 12.00; 13 -> 12.10; 14 -> 12.20; 15 -> 12.30; 16 -> 12.40; 17 -> 12.50
            18 -> 14.00; 19 -> 14.10; 20 -> 14.20; 21 -> 14.30; 22 -> 14.40; 23 -> 14.50
            24 -> 15.00; 25 -> 15.10; 26 -> 15.20; 27 -> 15.30; 28 -> 15.40; 29 -> 15.50
            30 -> 16.00; 31 -> 16.10; 32 -> 16.20; 33 -> 16.30; 34 -> 16.40; 35 -> 16.50
            else -> -1.0
        }
    }

    fun hasFraction(number: Double): Boolean {
        return number != number.toInt().toDouble()
    }

    fun onConfirm(onSuccess: () -> Unit = {}, onFail: (String) -> Unit = {}) {
        viewModelScope.launch {
            val time = appointment.appointmentDate
            if (time == null) {
                onFail("Vui lòng chọn thời gian hợp lệ.")
                return@launch
            }

            val isTaken = repository.isAppointmentSlotTaken(doctor1.id, time)
            if (isTaken) {
                onFail("Khung giờ này đã có người đặt. Vui lòng chọn slot khác.")
            } else {
                repository.setAppointment(doctor1.id, user.id, appointment)

                bookedSlots.value = bookedSlots.value + time

                // Gửi email xác nhận
                sendConfirmationEmail()

                appointment = Appointment()
                onSuccess()
            }
        }
    }

    private fun sendConfirmationEmail() {
        val email = user.email ?: return
        val patientName = user.name ?: "bạn"
        val doctorName = doctor1.name ?: "bác sĩ"
        val timeString = appointment.appointmentDate?.let { Date(it).toString() } ?: "không rõ thời gian"

        val subject = "✅ Xác nhận lịch hẹn khám bệnh tại Doctors Appointment"

        val body = """
        Kính gửi $patientName,

        Chúng tôi xin xác nhận bạn đã đặt lịch hẹn thành công với $doctorName.

        🕒 Thời gian: $timeString
        👨‍⚕️ Bác sĩ: $doctorName

        Vui lòng có mặt trước giờ hẹn 10 phút để được phục vụ tốt nhất.

        Nếu bạn cần hỗ trợ, vui lòng liên hệ với chúng tôi qua email hoặc hotline.

        Trân trọng,
        📍 Hệ thống đặt lịch Doctors Appointment
    """.trimIndent()

        Thread {
            try {
                val sender = MailSender(
                    senderEmail = "Phamlannhi2005@gmail.com", // Gmail hệ thống
                    senderPassword = "cynt etmj iwcq wvdi"     // App Password
                )
                sender.sendMail(email, subject, body)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun fetchBookedSlotsForDoctor(doctorId: String, date: Date) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

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
        }
    }
}
