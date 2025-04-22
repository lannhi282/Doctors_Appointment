package com.example.doctors_appointment.authentication

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
//sxswdcw
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun loginUser(authUser: AuthUser): Flow<ResultState<AuthResult>> {
        return flow {
            emit(ResultState.Loading())
            val result =
                firebaseAuth.signInWithEmailAndPassword(authUser.email, authUser.password).await()
            emit(ResultState.Success(result))
        }.catch {
            emit(ResultState.Error(it.message.toString()))
        }

    }

    override fun registerUser(authUser: AuthUser): Flow<ResultState<AuthResult>> {
        return flow {
            emit(ResultState.Loading())
            val result =
                firebaseAuth.createUserWithEmailAndPassword(authUser.email, authUser.password)
                    .await()
            emit(ResultState.Success(result))
            val user = firebaseAuth.currentUser

        }.catch {
            emit(ResultState.Error(it.message.toString()))
        }
    }
}