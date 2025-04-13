package com.example.doctors_appointment.ui.DoctorUI

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctors_appointment.MyApp
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.repository.FirestoreRepository
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.UiEvent
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date

class DoctorViewModel(
    val repository: FirestoreRepository
) : ViewModel() {


    var user = MyApp.doctor
    var selectedDate = mutableStateOf(Date())

    var newDoctor = Doctor().apply {
        id = user.id
        name = user.name
        email = user.email
        password = user.password
        contactNumber = user.contactNumber
        notification = user.notification
        gender = user.gender
        address = user.address
        rating = user.rating
        reviews = user.reviews // Adding all elements from user's reviews to newDoctor's reviews
        bmdcRegistrationNumber = user.bmdcRegistrationNumber
        qualifications = user.qualifications // Adding all elements from user's qualifications to newDoctor's qualifications
        about = user.about
        medicalSpecialty = user.medicalSpecialty
        profileImage = user.profileImage
        availabilityStatus = user.availabilityStatus // Adding all elements from user's availabilityStatus to newDoctor's availabilityStatus
        consultationFee = user.consultationFee
        experience = user.experience
        docoument = user.docoument // Adding all elements from user's docoument to newDoctor's docoument
        appointments = user.appointments // Adding all elements from user's appointments to newDoctor's appointments
    }


    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun OnEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.EditEmail -> {
                newDoctor.email = event.email
            }

            is ProfileEvent.EditGender -> {
                println("inside edit gender")
                newDoctor.gender = event.gender
                assert(newDoctor.gender == event.gender)
            }

            is ProfileEvent.EditName -> {
                newDoctor.name = event.name
            }

            is ProfileEvent.EditNumber -> {
                newDoctor.contactNumber = event.contact
            }

            is ProfileEvent.EditNotificationStatus -> {
                newDoctor.notification = event.notificationStatus
            }

            is ProfileEvent.AddQualification -> {
                newDoctor.qualifications = realmListOf(event.qualification) /// change further
            }

            is ProfileEvent.EditAbout -> {
                newDoctor.about = event.about
            }

            is ProfileEvent.EditBMDCNo -> {
                newDoctor.bmdcRegistrationNumber = event.bmdcNo
            }

            is ProfileEvent.EditExperience -> {
                newDoctor.experience = event.experience
            }

            is ProfileEvent.EditMedicalSpeciality -> {
                newDoctor.medicalSpecialty = event.medicalSpeciality
            }

            is ProfileEvent.EditAddress -> {
                newDoctor.address = event.address
            }

            is ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    repository.updateDoctor(newDoctor)
                    user = newDoctor
                }
            }


            else -> {}
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {  // sends flow throw the channel
        viewModelScope.launch {   // this binds the lifecycle of coroutine with our viewmodel
            _uiEvents.send(uiEvent)
        }
    }

    fun getTime(slot: Int): Double {
        return when (slot) {
            0 -> 10.00
            1 -> 10.10
            2 -> 10.20
            3 -> 10.30
            4 -> 10.40
            5 -> 10.50
            6 -> 11.00
            7 -> 11.10
            8 -> 11.20
            9 -> 11.30
            10 -> 11.40
            11 -> 11.50
            12->12.00
            13->12.10
            14->12.20
            15->12.30
            16->12.40
            17->12.50
            18->2.00
            19->2.10
            20->2.20
            21->2.30
            22->2.40
            23->2.50
            24->3.00
            25->3.10
            26->3.20
            27->3.30
            28->3.40
            29->3.50
            30->4.00
            31->4.10
            32->4.20
            33->4.30
            34->4.40
            35->4.50

            else -> -1.0
        }
    }
}