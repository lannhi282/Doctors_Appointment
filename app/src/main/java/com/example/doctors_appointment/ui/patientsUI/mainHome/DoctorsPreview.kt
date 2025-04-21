package com.example.doctors_appointment.ui.patientsUI.mainHome

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.data.repository.FirestoreRepositoryImpl
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.ui.patientsUI.viewmodels.MainHomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable


@Composable
fun DoctorsPreview(
    navController: NavController,
    mainhomeViewModel: MainHomeViewModel
) {

//    val availableDoctors = mainhomeViewModel.doctors.value
    val availableDoctors by mainhomeViewModel.doctors.collectAsState()

    Text(
        text = " Doctors",
        fontFamily = fontInria,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge
    )

    LazyRow {
        items(
            items = availableDoctors,
            key = { it.id } // 👈 BẮT BUỘC
        ) { doctor ->
            DoctorsCard(
                doctor = doctor,
                updateRating = {
                    val afterUpdated = doctor.copy(rating = it)
                    mainhomeViewModel.updateDoctor(afterUpdated)
                },
                navController = navController
            )
        }
    }
}

@Composable
fun DoctorsCard(
    doctor: Doctor,
    updateRating: (Double) -> Unit,
    navController: NavController
) {

    Box(
        modifier = Modifier
            .height(200.dp)
            .width(280.dp)
            .padding(end = 15.dp, bottom = 5.dp, top = 5.dp)
            .clip(RoundedCornerShape(10))
            .border(2.dp, Indigo500, RoundedCornerShape(10))
            //.background(Indigo50)
            .clickable {
                navController.navigate(Screen.doctorsDetails.withArgs(doctor.id))
            }

    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 25.dp, end = 10.dp, bottom = 8.dp)
        ) {

            Text(
                text = doctor.name,
                fontSize = 15.sp,
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = doctor.medicalSpecialty,
                fontSize = 10.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
            Text(
                text = "\nQualifications:",
                fontSize = 12.sp,
                fontFamily = fontInria,
                color = Color.Black
            )
            val qualificationText = doctor.qualifications.joinToString(", ")
            Text(
                text = qualificationText,
                fontSize = 8.sp,
                fontFamily = fontInria,
                color = Indigo900
            )
            Text(
                text = "\nRating:",
                fontSize = 12.sp,
                fontFamily = fontInria,
                color = Color.Black
            )
            Text(
                text = String.format("%.2f", doctor.rating),
                fontSize = 8.sp,
                fontFamily = fontInria,
                color = Indigo900
            )

            // ⭐ Displaying clickable stars
            // 1. Dùng state để phản hồi UI ngay
            var ratingState by remember { mutableStateOf(doctor.rating) }

            // 2. Nếu doctor.rating thay đổi (do Firebase realtime), cập nhật lại state
            LaunchedEffect(doctor.rating) {
                ratingState = doctor.rating
            }

            val stars = generateStars(ratingState)

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stars.forEachIndexed { index, starIcon ->
                    Icon(
                        imageVector = starIcon,
                        contentDescription = null,
                        tint = Indigo900,
                        modifier = Modifier
                            .width(20.dp)
                            .clickable {
                                val newRating = index + 1.0
                                ratingState = newRating // cập nhật UI ngay
                                updateRating(newRating)
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        FirestoreRepositoryImpl.updateDoctorRating(doctor.id, newRating)
                                    } catch (e: Exception) {
                                        Log.e("⭐", "Rating update failed: ${e.message}")
                                    }
                                }
                            }

                    )
                }
            }
        }
    }
}


@Composable
fun generateStars(rating: Double): List<ImageVector> {
    val fullStars = rating.toInt()
    val hasHalfStar = rating % 1 != 0.0

    return List(5) { index ->
        if (index < fullStars) {
            Icons.Default.Star
        } else if (hasHalfStar && index == fullStars) {
            Icons.Default.StarHalf
        } else {
            Icons.Outlined.StarOutline
        }
    }
}