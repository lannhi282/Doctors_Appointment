package com.example.doctors_appointment.authentication

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
//sấdsadcsDX
interface AuthRepository {

    fun loginUser(
        authUser: AuthUser
    ): Flow<ResultState<AuthResult>>

    fun registerUser(
        authUser: AuthUser
    ): Flow<ResultState<AuthResult>>

}