package com.example.doctors_appointment.util

sealed class UiEvent{
    data class Login(
        val email: String,
        val password: String
    ): UiEvent()

    data class SignUp(
        val email: String,
        val name: String,
        val password: String,
        val confirmPassword: String,
        val asPatient: Boolean
    ): UiEvent()

    data class ShowSnackBar(
        val massage: String
    ): UiEvent()

    data class Navigate(val route: String): UiEvent()


}