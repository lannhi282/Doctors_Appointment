package com.example.doctors_appointment

import android.app.Application
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.model.Patient
import com.example.doctors_appointment.util.Constants
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var patient: Patient
        lateinit var doctor: Doctor
    }
    override fun onCreate() {
        super.onCreate()
        // Khởi tạo biến patient tại đây
        
        patient = Patient()
        doctor = Doctor()
    }
}