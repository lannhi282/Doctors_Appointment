package com.example.doctors_appointment.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontActor
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.ui.theme.Indigo100
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.util.UiEvent
import androidx.compose.foundation.background
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUp(
    navController: NavController,
    signUpViewModel: SignUpViewModel
){

    val snackBarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {

        LaunchedEffect(key1 = true){
            signUpViewModel.uiEvents.collect{ event ->
                when(event) {

                    is UiEvent.Navigate -> {
                        navController.navigate(event.route)
                    }
                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(message = event.massage, duration = SnackbarDuration.Short)
                    }
                    else -> Log.d("alfkj", "ui event are not working")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Login image",
            modifier = Modifier.size(150.dp))
        Text(
            text = "Create Account",
            textAlign = TextAlign.Center,
            fontFamily = fontInria,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold

        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "Already have an Account?",
            fontFamily = fontInria,
            fontSize = 14.sp,
            modifier = Modifier.alpha(1f),
            fontWeight = FontWeight.Medium,
        )
        TextButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)

        ) {
            Text(text = "Sign in",
                fontFamily = fontInria,
                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(1f),
                color = Indigo900,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // textfield for name

        var filledName by remember{
            mutableStateOf("")
        }
//        OutlinedTextField(
//            value = filledName ,
//            onValueChange = {
//                filledName = it
//            },
//            label = {
//                Text(text = "Enter your name:")
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Outlined.Person,
//                    contentDescription = "name" )
//            }
//        )
        OutlinedTextField(
            value = filledName,
            onValueChange = { filledName = it },
            label = { Text("Enter your name:") },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = "name")
            },
            shape = RoundedCornerShape(14.dp), // 🌟 bo góc đẹp
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,     // khi focus
                unfocusedBorderColor = Indigo100,   // khi chưa focus
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ),
            modifier = Modifier
                .width(300.dp) // tăng chiều rộng cho thoáng
        )

        Spacer(modifier = Modifier.height(5.dp))

        // textfield for mail

        var filledMail by remember{
            mutableStateOf("")
        }
        OutlinedTextField(
            value = filledMail,
            onValueChange = { filledMail = it },
            label = { Text("Enter Email:") },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email")
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,
                unfocusedBorderColor = Indigo100,
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ),
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        var filledPass by remember {
            mutableStateOf("")
        }
        OutlinedTextField(
            value = filledPass,
            onValueChange = { filledPass = it },
            label = { Text("Enter Password:") },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Password, contentDescription = "Pass")
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,
                unfocusedBorderColor = Indigo100,
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ),
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        var filledPass1 by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = filledPass1, onValueChange = { filledPass1 = it }, label = { Text("Confirm Password:") }, leadingIcon = {
                Icon(imageVector = Icons.Outlined.Password, contentDescription = "Pass")
            }, shape = RoundedCornerShape(14.dp), colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,
                unfocusedBorderColor = Indigo100,
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ), modifier = Modifier.width(300.dp))
        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {
                signUpViewModel.OnSignUpClick(filledMail, filledName,filledPass, filledPass1, true)
            },
            modifier = Modifier
                .height(50.dp)
                .width(210.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Indigo100,
                contentColor = Indigo900
            )
        ) {
            Text(
                text = "As a Patient",
                fontSize = 24.sp,
                //fontWeight = FontWeight.Bold,
                fontFamily = fontActor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                signUpViewModel.OnSignUpClick(filledMail,filledName,filledPass, filledPass1, false)
            },
            modifier = Modifier
                .height(50.dp)
                .width(210.dp) ,
        ) {
            Text(
                text = "As a Doctor",
                fontSize = 24.sp,
                //fontWeight = FontWeight.Bold,
                fontFamily = fontActor
            )
        }

    }
}