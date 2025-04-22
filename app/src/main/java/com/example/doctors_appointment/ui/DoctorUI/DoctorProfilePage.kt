package com.example.doctors_appointment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Details
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doctors_appointment.R

import com.example.doctors_appointment.ui.theme.Indigo200
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.theme.Indigo500
import com.example.doctors_appointment.ui.theme.Indigo900
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import com.example.doctors_appointment.data.model.Doctor
import com.example.doctors_appointment.ui.patientsUI.mainHome.RoundImage
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.DoctorUI.DoctorViewModel
import com.example.doctors_appointment.ui.patientsUI.BottomNavigationItem
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontActor
import com.example.doctors_appointment.ui.patientsUI.viewmodels.OthersViewModel
import com.example.doctors_appointment.ui.theme.Indigo400
import com.example.doctors_appointment.util.ProfileEvent
import com.example.doctors_appointment.util.Screen

import com.google.firebase.auth.FirebaseAuth

@Composable
fun DoctorProfilePage(
    onSignOut: () -> Unit,
    doctorViewModel: DoctorViewModel
) {

    var onEdit by remember {
        mutableStateOf(false)
    }
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .background(Indigo50)
            .padding(10.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val items = listOf(

                BottomNavigationItem(
                    title = "About",
                    route = "about",
                    selectedIcon = Icons.Filled.Description,
                    unselectedIcon = Icons.Outlined.Description,
                    hasNews = false
                ),
                BottomNavigationItem(
                    title = "Details",
                    route = "details",
                    selectedIcon = Icons.Filled.Details,
                    unselectedIcon = Icons.Outlined.Details,
                    hasNews = false
                )
            )

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .weight(.8f)
            ) {
                items.fastForEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedTabIndex)
                                    item.selectedIcon else item.unselectedIcon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .weight(.15f)
                    .padding(top = 10.dp),
                onClick = {
                    onEdit = !onEdit
                }
            ) {
                Icon(
                    imageVector = if (onEdit) Icons.Filled.Edit else Icons.Outlined.Edit,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            RoundImage(
                image = painterResource(id = R.drawable.man),
                modifier = Modifier.height(80.dp)
            )

            var filledName by remember {
                mutableStateOf(doctorViewModel.user.name)
            }

            if (onEdit) {
                OutlinedTextField(

                    value = filledName,
                    onValueChange = { newText ->
                        filledName = newText
                        doctorViewModel.OnEvent(ProfileEvent.EditName(newText))
                    },
                    label = {
                        Text(
                            text = "Name",
                            fontFamily = fontInria
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, // Text color
                        unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                    ),
                )
            } else {
                Text(
                    text = doctorViewModel.user.name,
                    fontFamily = fontInria,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${doctorViewModel.user.medicalSpecialty} Specialist",
                    fontFamily = fontInria,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Indigo900,
                        modifier = Modifier.width(20.dp)
                    )

                    Text(
                        text = String.format("%.2f", doctorViewModel.user.rating),
                        fontSize = 15.sp,
                        fontFamily = fontInria,
                        color = Indigo900
                    )

                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

                if (onEdit) {
                    if (selectedTabIndex == 1) {
                        EditDoctorDetails(
                            doctorViewModel.user,
                            doctorViewModel
                        )
                    } else {
                        editAbout(doctor = doctorViewModel.user, doctorViewModel = doctorViewModel)
                    }
                    OutlinedButton(
                        onClick = {
                            doctorViewModel.OnEvent(ProfileEvent.OnSave)
                            onEdit = !onEdit
                        },
                    ) {
                        Text(
                            text = "SAVE",
                            fontSize = 20.sp,
                            fontFamily = fontInria,
                        )
                    }
                } else {
                    if (selectedTabIndex == 1) {
                        com.example.doctors_appointment.ui.patientsUI.DoctorsDetails(
                            doctorViewModel.user,
                            isPatient = false,
                            onSignOut = onSignOut
                        )
                    } else com.example.doctors_appointment.ui.patientsUI.About(doctorViewModel.user)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDoctorDetails(
    doctor: Doctor,
    doctorViewModel: DoctorViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5))
//            .border(2.dp, Indigo500, RoundedCornerShape(5))
            .background(Color.White)

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, top = 12.dp, end = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {

            var filledAddress by remember {
                mutableStateOf(doctor.address)
            }
            OutlinedTextField(

                value = filledAddress,
                onValueChange = { newText ->
                    filledAddress = newText
                    doctorViewModel.OnEvent(ProfileEvent.EditAddress(filledAddress))
                },
                label = {
                    Text(
                        text = "Address",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            var filledMedicalSpeciality by remember {
                mutableStateOf(doctor.medicalSpecialty)
            }
            OutlinedTextField(

                value = filledMedicalSpeciality,
                onValueChange = { newText ->
                    filledMedicalSpeciality = newText
                    doctorViewModel.OnEvent(
                        ProfileEvent.EditMedicalSpeciality(
                            filledMedicalSpeciality
                        )
                    )
                },
                label = {
                    Text(
                        text = "Medical Speciality",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            var filledGender by remember {
                mutableStateOf(if (doctor.gender == null) "Unknown" else if (doctor.gender == true) "Male" else "Female")
            }
            var isExpanded by remember {
                mutableStateOf(false)
            }

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {

                OutlinedTextField(

                    value = filledGender,
                    onValueChange = {
                        filledGender = it
                    },
                    label = {
                        Text(
                            text = "Gender"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, // Text color
                        unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    modifier = Modifier.menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {

                    listOf("Male", "Female").forEach { gender ->
                        DropdownMenuItem(
                            text = {
                                Text(text = gender, fontFamily = fontInria)
                            },
                            onClick = {
                                filledGender = gender
                                isExpanded = false
                                val genderValue = when (gender) {
                                    "Male" -> true
                                    else -> false
                                }
                                doctorViewModel.OnEvent(ProfileEvent.EditGender(genderValue))
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(5.dp))


            var filledNumber by remember {
                mutableStateOf(doctor.contactNumber)
            }
            OutlinedTextField(

                value = filledNumber,
                onValueChange = { newText ->
                    filledNumber = newText
                    doctorViewModel.OnEvent(ProfileEvent.EditNumber(filledNumber))
                },
                label = {
                    Text(
                        text = "Contact Number",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            Spacer(modifier = Modifier.height(5.dp))

            var filledMail by remember {
                mutableStateOf(doctor.email)
            }
            OutlinedTextField(

                value = filledMail,
                onValueChange = { newText ->
                    filledMail = newText
                    doctorViewModel.OnEvent(ProfileEvent.EditEmail(filledMail))
                },
                label = {
                    Text(
                        text = "Email",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            Spacer(modifier = Modifier.height(5.dp))

            var filledQualification by remember {
                mutableStateOf(doctor.qualifications.toString())
            }
            OutlinedTextField(

                value = filledQualification,
                onValueChange = { newText ->
                    filledQualification = newText
                    doctorViewModel.OnEvent(ProfileEvent.AddQualification(newText))
                },
                label = {
                    Text(
                        text = "Qualifications",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            Spacer(modifier = Modifier.height(5.dp))

            var filledBMDCNo by remember {
                mutableStateOf(doctor.bmdcRegistrationNumber)
            }
            OutlinedTextField(

                value = filledBMDCNo,
                onValueChange = { newText ->
                    filledBMDCNo = newText
                    doctorViewModel.OnEvent(ProfileEvent.EditBMDCNo(filledBMDCNo))
                },
                label = {
                    Text(
                        text = "License Code",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            Spacer(modifier = Modifier.height(5.dp))

            var filledExperience by remember {
                mutableStateOf(doctor.experience)
            }
            OutlinedTextField(

                value = filledExperience.toString(),
                onValueChange = { newText ->

                    filledExperience = try {
                        newText.toInt()
                    } catch (e: Exception) {
                        doctor.experience
                    }
                    doctorViewModel.OnEvent(ProfileEvent.EditExperience(filledExperience))
                },
                label = {
                    Text(
                        text = "Experience",
                        fontFamily = fontInria
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Text color
                    unfocusedContainerColor = Color.Transparent, // Background color
//                        focusedIndicatorColor = Color.Transparent, // No indicator when focused
//                        unfocusedIndicatorColor = Color.Transparent // No indicator when not focused
                ),
            )

            Spacer(modifier = Modifier.height(5.dp))

            val notification: Boolean = if (doctor.notification == null) {
                true
            } else doctor.notification!!

            var notificationStatus by remember {
                mutableStateOf(notification)
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Notification:",
                    fontSize = 25.sp,
                    fontFamily = fontInria,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))


                Switch(

                    checked = notificationStatus,
                    onCheckedChange = {
                        notificationStatus = it
                        doctorViewModel.OnEvent(
                            ProfileEvent.EditNotificationStatus(
                                notificationStatus
                            )
                        )
                    },
                    thumbContent = {
                        if (notificationStatus) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
        }
    }
}

@Composable
fun editAbout(
    doctor: Doctor,
    doctorViewModel: DoctorViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5))
            .border(2.dp, Indigo500, RoundedCornerShape(5))
            .background(Color.White)
            .padding(15.dp)
    ) {
        var filledAbout by remember {
            mutableStateOf(doctor.about)
        }
        OutlinedTextField(

            value = filledAbout,
            onValueChange = { newText ->
                filledAbout = newText
                doctorViewModel.OnEvent(ProfileEvent.EditAbout(filledAbout))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, // Text color
                unfocusedContainerColor = Color.Transparent, // Background color
                focusedIndicatorColor = Color.Transparent, // No indicator when focused
                unfocusedIndicatorColor = Color.Transparent // No indicator when not focused

            ),
        )
    }
}