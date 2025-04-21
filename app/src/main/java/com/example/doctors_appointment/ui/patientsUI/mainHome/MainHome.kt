package com.example.doctors_appointment.ui.patientsUI.mainHome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.patientsUI.viewmodels.MainHomeViewModel

var fontInria = FontFamily(
    Font(R.font.inriasans_regular, FontWeight.Normal),
    Font(R.font.inriasans_bold, FontWeight.Bold),
    Font(R.font.inriasans_light, FontWeight.Light)
)
var fontActor = FontFamily(
    Font(R.font.actor)
)

@Composable
fun MainHome(
    navController: NavController,
    mainHomeViewModel: MainHomeViewModel
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ){
        TopBar(
            name = mainHomeViewModel.patient.name,
            profileImage = painterResource(id = R.drawable.man),
        )

        FeaturedItems()

        CategoryRow(navController, mainHomeViewModel)

        DoctorsPreview(navController, mainHomeViewModel)

    }
}

@Composable
fun TopBar(
    name: String,
    profileImage: Painter,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Indigo50)

    ){

        RoundImage(image = profileImage)
        Text(
            text = name,
            overflow = TextOverflow.Ellipsis,
            fontFamily = fontInria,
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "notification",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 10.dp)
        )

    }
}


@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier.height(40.dp)
){
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
    )
}