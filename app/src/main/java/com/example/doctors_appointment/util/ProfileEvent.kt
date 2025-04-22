package com.example.doctors_appointment.util

import com.example.doctors_appointment.data.model.Appointment

sealed class ProfileEvent {
    data class EditName(val name: String) : ProfileEvent()
    data class EditEmail(val email: String) : ProfileEvent()
    data class EditNumber(val contact: String) : ProfileEvent()
    data class EditGender(val gender: Boolean) : ProfileEvent()
    data class EditHeight(val height: Double) : ProfileEvent()
    data class EditWeight(val weight: Double) : ProfileEvent()
    data class EditDoT(val dot: String) : ProfileEvent()
    data class EditNotificationStatus(val notificationStatus: Boolean) : ProfileEvent()
    data class EditAbout(val about: String) : ProfileEvent()
    data class EditMedicalSpeciality(val medicalSpeciality: String) : ProfileEvent()
    data class EditBMDCNo(val bmdcNo: String) : ProfileEvent()
    data class AddQualification(val qualification: String) : ProfileEvent()
    data class EditExperience(val experience: Int) : ProfileEvent()
    data class EditAddress(val address: String) : ProfileEvent()
    data class EditMedicalHis(val medicalHis: List<Appointment>): ProfileEvent()
    data class UpdateProfileImage(val imageUri: String): ProfileEvent()

    object OnSave : ProfileEvent()
}