package com.example.doctors_appointment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.authentication.AuthRepository
import com.example.doctors_appointment.authentication.AuthUser
import com.example.doctors_appointment.authentication.ResultState
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.util.UiEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiEvents = Channel<UiEvent>()
    private val auth = FirebaseAuth.getInstance()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnSignUpClick(
        email: String,
        name: String,
        password: String,
        confirmPassword: String,
        asPatient: Boolean
    ) {
        // 1. Kiểm tra đầu vào
        when {
            email.isBlank() || name.isBlank() -> {
                sendUiEvent(UiEvent.ShowSnackBar("Name or email cannot be empty!"))
                return
            }
            !isValidEmail(email) -> {
                sendUiEvent(UiEvent.ShowSnackBar("Invalid email format!"))
                return
            }
            password != confirmPassword -> {
                sendUiEvent(UiEvent.ShowSnackBar("Passwords do not match!"))
                return
            }
            password.length < 8 -> {
                sendUiEvent(UiEvent.ShowSnackBar("Password must be at least 8 characters!"))
                return
            }
        }

        // 2. Đăng ký người dùng
        viewModelScope.launch {
            val authUser = AuthUser(email, password)
            authRepository.registerUser(authUser).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        sendUiEvent(UiEvent.ShowSnackBar("Registering user, please wait..."))
                    }
                    is ResultState.Success -> {
                        // 3. Tạo Patient hoặc Doctor
                        val userId = auth.currentUser?.uid
                        if (userId == null) {
                            sendUiEvent(UiEvent.ShowSnackBar("Failed to get user ID."))
                            return@collect
                        }

                        val creationResult = if (asPatient) {
                            createPatient(userId, name, email)
                        } else {
                            createDoctor(userId, name, email)
                        }

                        // 4. Xử lý kết quả tạo profile
                        when (creationResult) {
                            is ResultState.Success -> {
                                sendUiEvent(UiEvent.Navigate(Screen.signIn.route))
                                sendUiEvent(UiEvent.ShowSnackBar("Sign Up Successful. Please Login."))
                            }
                            is ResultState.Error -> {
                                sendUiEvent(UiEvent.ShowSnackBar("Failed to create profile: ${creationResult.message}"))
                            }
                            else -> {
                                sendUiEvent(UiEvent.ShowSnackBar("Unexpected error during profile creation."))
                            }
                        }
                    }
                    is ResultState.Error -> {
                        println("Authentication error: ${result.message}")
                        val errorMessage = when (result.message) {
                            "The email address is already in use by another account." -> "Email already in use."
                            else -> "Authentication failed: ${result.message}"
                        }
                        sendUiEvent(UiEvent.ShowSnackBar(errorMessage))
                    }
                    else -> {
                        sendUiEvent(UiEvent.ShowSnackBar("Unexpected error during registration."))
                    }
                }
            }
        }
    }

    private suspend fun createPatient(userId: String, name: String, email: String): ResultState<Unit> {
        return try {
            val patient = Patient().apply {
                id = userId
                this.name = name
                this.email = email
            }
            val signUpOp = SignUpOp()
            signUpOp.insertPatient(patient) // Giả định hàm này trả về ResultState<Unit>
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to create patient")
        }
    }

    private suspend fun createDoctor(userId: String, name: String, email: String): ResultState<Unit> {
        return try {
            val doctor = Doctor().apply {
                id = userId
                this.name = name
                this.email = email
            }
            val signUpOp = SignUpOp()
            signUpOp.insertDoctor(doctor) // Giả định hàm này trả về ResultState<Unit>
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to create doctor")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(uiEvent)
        }
    }
}