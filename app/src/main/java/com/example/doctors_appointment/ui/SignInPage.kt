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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontActor
import com.example.doctors_appointment.ui.patientsUI.mainHome.fontInria
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.theme.Indigo100
import com.example.doctors_appointment.ui.theme.Indigo900
import com.example.doctors_appointment.util.UiEvent
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignIn(
    navController: NavController,
    signInViewModel: SignInViewModel
){

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {

        // handling one time event received from viewmodel

        LaunchedEffect(key1 = true){
            signInViewModel.uiEvents.collect{ event ->
                when(event) {
                    is UiEvent.Navigate -> {
                        Log.d("dfkl", "login navigate")
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

        Image(painter = painterResource(id = R.drawable.a), contentDescription = "Login image",
            modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Welcome to Doctor's Appointment",
            textAlign = TextAlign.Center,
            fontFamily = fontInria,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold

        )

        Spacer(
            modifier = Modifier.height(60.dp)
        )

        Text(
            text = "Sign in to continue",
            fontFamily = fontInria,
            fontSize = 13.sp,
            modifier = Modifier.alpha(1f),
            fontWeight = FontWeight.Medium,
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        var filledMail by remember{
            mutableStateOf("lamlt@gmail.com")
            // Nhập sẵn email ở đây luôn
        }
        OutlinedTextField(
            value = filledMail,
            onValueChange = { filledMail = it },
            label = { Text("Enter Email address:") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email"
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User"
                )
            },
            shape = RoundedCornerShape(14.dp), // ✅ Bo góc mềm mại
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,     // ✅ Khi focus
                unfocusedBorderColor = Indigo100,   // ✅ Khi chưa focus
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ),
            isError = false, // ✅ Để false nếu không báo lỗi
            modifier = Modifier.width(300.dp)
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )


        var filledPass by remember { mutableStateOf("lamlt123@") }
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = filledPass,
            onValueChange = { filledPass = it },
            label = { Text("Enter your Password:") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Password,
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                val visibilityIcon = if (isPasswordVisible)
                    Icons.Filled.VisibilityOff
                else
                    Icons.Filled.Visibility

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Indigo900,
                unfocusedBorderColor = Indigo100,
                cursorColor = Indigo900,
                focusedLabelColor = Indigo900
            ),
            modifier = Modifier.width(300.dp)
        )
        Spacer(
            modifier = Modifier.height(60.dp)
        )

        Button(
            onClick = {
                signInViewModel.OnLoginClick(filledMail, filledPass)
            },
            modifier = Modifier
                .height(50.dp)
                .width(150.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Indigo100,
                contentColor = Indigo900
//                containerColor = Indigo900, // màu đậm hơn
//                contentColor = Color.White
            )
        ) {
            Text(
                text = "Log in",
                fontSize = 24.sp,
                //fontWeight = FontWeight.Bold,
                fontFamily = fontActor
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )



        Text(
            text = "Don't Have an Account?",
            fontFamily = fontInria,
            fontSize = 12.sp,
            modifier = Modifier.alpha(1f),
            fontWeight = FontWeight.Medium,
        )
        TextButton(
            onClick = {
                navController.navigate(Screen.signUp.route)
            },
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)

        ) {
            Text(text = "Sign Up",
                fontFamily = fontInria,
                fontSize = 12.sp,
                modifier = Modifier.alpha(1f),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}