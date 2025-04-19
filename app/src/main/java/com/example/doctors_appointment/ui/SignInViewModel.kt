package com.example.doctors_appointment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _uiEvents = Channel<UiEvent>()                  // mutable hence made private(so that no one can change it outside the viewmodel) for sending event
    private val auth = FirebaseAuth.getInstance()
    val uiEvents = _uiEvents.receiveAsFlow()                   // immutable to use in the outside viewModel (UI) for collecting the flow

//    init {
//        if (auth.currentUser != null) {
//            viewModelScope.launch {
//                val isPatient = CheckUser()
//
//                if (isPatient) {
//                    sendUiEvent(UiEvent.Navigate(Screen.mainHome.route))
//                } else {
//                    sendUiEvent(UiEvent.Navigate(Screen.doctorNavBar.route))
//                }
//
//                //sendUiEvent(UiEvent.Navigate(Screen.checkUser.withArgs(email, password)))
//
//            }
//        }
//    }

    fun OnLoginClick(email: String, password: String) {

        // firebase authentication

        val authUser = AuthUser(email, password)

        viewModelScope.launch {
            authRepository.loginUser(authUser).collect { result ->
                when (result) {
                    is ResultState.Success -> {

                        val isPatient = CheckUser()

                        if (isPatient) {
                            sendUiEvent(UiEvent.Navigate(Screen.mainHome.route))
                        } else {
                            sendUiEvent(UiEvent.Navigate(Screen.doctorNavBar.route))
                        }

                        //sendUiEvent(UiEvent.Navigate(Screen.checkUser.withArgs(email, password)))

                    }

                    is ResultState.Loading -> {
                        sendUiEvent(UiEvent.ShowSnackBar("Wait for a while for authentication."))
                    }

                    is ResultState.Error -> {
                        sendUiEvent(UiEvent.ShowSnackBar("Invalid email or password."))
                    }
                }

            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent){  // sends flow throw the channel
        viewModelScope.launch{   // this binds the lifecycle of coroutine with our viewmodel
            _uiEvents.send(uiEvent)
        }
    }

}