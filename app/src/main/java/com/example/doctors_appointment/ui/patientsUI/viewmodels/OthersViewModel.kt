package com.example.doctors_appointment.ui.patientsUI.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Appointment
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OthersViewModel(
    private val repository: FirestoreRepository,
    private val navController: NavController
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var doctors = mutableStateOf(emptyList<Doctor>())
    var categoryDoctors = mutableStateOf(emptyList<Doctor>())
    var doctor = Doctor()
    var upcomingAppointments = mutableStateOf(emptyList<Appointment>())
    var pastAppointments = mutableStateOf(emptyList<Appointment>())
    var user = MyApp.patient

    var newPatient: Patient = Patient().apply {
        id = user.id
        name = user.name
        email = user.email
        gender = user.gender
        contactNumber = user.contactNumber
        height = user.height
        weight = user.weight
        notification = user.notification
        medicalHistory = user.medicalHistory
        dateOfBirth = user.dateOfBirth
        profileImage = user.profileImage
        password = user.password
    }

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EditEmail -> user.email = event.email
            is ProfileEvent.EditGender -> user.gender = event.gender
            is ProfileEvent.EditName -> user.name = event.name
            is ProfileEvent.EditHeight -> user.height = event.height
            is ProfileEvent.EditWeight -> user.weight = event.weight
            is ProfileEvent.EditDoT -> user.dateOfBirth = event.dot
            is ProfileEvent.EditNumber -> user.contactNumber = event.contact
            is ProfileEvent.EditNotificationStatus -> user.notification = event.notificationStatus
            is ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    repository.updatePatient(user)
                    MyApp.patient = user
                }
            }
            else -> {}
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }

    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            try {
                val fileUri = Uri.parse(imageUri)
                val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference
                val avatarRef = storageRef.child("avatars/${user.id}.jpg")

                // Upload file lên Firebase Storage
                val uploadTask = avatarRef.putFile(fileUri)
                uploadTask.addOnSuccessListener {
                    avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val url = downloadUri.toString()

                        // Cập nhật ảnh đại diện trong user
                        user.profileImage = Blob.fromBytes(byteArrayOf())

                        // Cập nhật lên Firestore
                        viewModelScope.launch {
                            repository.updatePatient(user)
                            MyApp.patient = user
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.e("🔥 Upload error", "Upload thất bại: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("🔥 Storage error", "Lỗi xử lý ảnh: ${e.message}")
            }
        }
    }

    fun saveImageBinaryToFirestoreInMedicalHistory(
        context: Context,
        uri: Uri,
        patientId: String
    ) {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return
        val byteArray = inputStream.readBytes()

        // Kiểm tra giới hạn dung lượng
        if (byteArray.size > 1024 * 1024) {
            Log.e("Firestore", "Ảnh quá lớn, vượt quá 1MB!")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val medicalHistoryUpdate = mapOf(
            "profileImage" to com.google.firebase.firestore.Blob.fromBytes(byteArray)
        )

        db.collection("patients").document(patientId)
            .update(medicalHistoryUpdate)
            .addOnSuccessListener {
                Log.d("Firestore", "Lưu ảnh nhị phân vào medicalHistory thành công.")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Lỗi khi lưu ảnh: ${it.message}")
            }
    }

    fun fetchProfileImageAsBitmap(onResult: (Bitmap?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("patients")
            .document(user.id)
            .get()
            .addOnSuccessListener { doc ->
                val blob = doc.get("profileImage") as? com.google.firebase.firestore.Blob
                val bytes = blob?.toBytes()
                if (bytes != null) {
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    onResult(bitmap)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Lỗi khi lấy ảnh nhị phân: ${it.message}")
                onResult(null)
            }
    }


    fun getDoctorFromId(userId: String) {
        viewModelScope.launch {
            repository.getDoctorById(userId)?.let {
                doctor = it
            }
        }
    }

    fun getDoctorFromCategory(category: String) {
        viewModelScope.launch {
            categoryDoctors.value = repository.getDoctorsByCategory(category)
        }
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            repository.updatePatient(patient)
            MyApp.patient = patient
        }
    }

    init {
        observeDoctorsRealtime()
        refreshAppointments()
    }

    fun refreshAppointments() {
        viewModelScope.launch {
            upcomingAppointments.value = repository.getUpcomingAppointments(user.id, isDoctor = false)
            pastAppointments.value = repository.getPastAppointments(user.id, isDoctor = false)
        }
    }

    suspend fun updateUpcomingAppointments() {
        upcomingAppointments.value = repository.getUpcomingAppointments(user.id, isDoctor = false)
    }

    suspend fun getDoctorById(id: String): Doctor? {
        return repository.getDoctorById(id)
    }

    fun signout() {
        auth.signOut()
        navController.navigate(Screen.signIn.route)
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                repository.deleteAppointment(appointment)
                refreshAppointments()
            } catch (e: Exception) {
                Log.e("🔥 DELETE ERROR", "Không thể xoá lịch hẹn: ${e.message}")
            }
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                repository.updateAppointment(appointment)
                refreshAppointments()
            } catch (e: Exception) {
                Log.e("🔥 UPDATE ERROR", "Không thể cập nhật lịch hẹn: ${e.message}")
            }
        }
    }

    fun insertAppointmentAndRefresh(appointment: Appointment) {
        viewModelScope.launch {
            try {
                repository.insertAppointment(appointment)
                refreshAppointments()
            } catch (e: Exception) {
                Log.e("🔥 INSERT ERROR", "Không thể thêm lịch hẹn: ${e.message}")
            }
        }
    }

    fun observeDoctorsRealtime() {
        val db = FirebaseFirestore.getInstance()
        db.collection("doctors")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("🔥 Firestore", "Listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val updatedDoctors = snapshot.toObjects(Doctor::class.java)
                    doctors.value = updatedDoctors
                }
            }
    }


    fun changePassword(currentPassword: String, newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = this.user.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    sendUiEvent(UiEvent.ShowToast("Đổi mật khẩu thành công"))
                                    sendUiEvent(UiEvent.NavigateBack)
                                } else {
                                    sendUiEvent(UiEvent.ShowToast("Không thể đổi mật khẩu"))
                                }
                            }
                    } else {
                        sendUiEvent(UiEvent.ShowToast("Mật khẩu hiện tại không đúng"))
                    }
                }
        }
    }
}