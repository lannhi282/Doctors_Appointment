package com.example.doctors_appointment.ui.patientsUI.mainHome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doctors_appointment.R
import com.example.doctors_appointment.util.Screen
import com.example.doctors_appointment.ui.theme.Indigo50
import com.example.doctors_appointment.ui.patientsUI.viewmodels.MainHomeViewModel

data class Category(
    val name: String,
    val icon: Painter
)

@Composable
fun CategoryRow(
    navController: NavController,
    mainHomeViewModel: MainHomeViewModel
){
    val categories = listOf(
        Category(
            name = "Teeth",
            icon = painterResource(id = R.drawable.teeth)
        ),
        Category(
            name = "Jaw",
            icon = painterResource(id = R.drawable.jaw)
        ),
        Category(
            name = "Face",
            icon = painterResource(id = R.drawable.face)
        ),
        Category(
            name = "Whitening",
            icon = painterResource(id = R.drawable.white)
        ),
        Category(
            name = "Braces",
            icon = painterResource(id = R.drawable.braces)
        ),
        Category(
            name = "Extraction",
            icon = painterResource(id = R.drawable.extraction)
        ),
        Category(
            name = "Filling",
            icon = painterResource(id = R.drawable.filling)
        ),
        Category(
            name = "Veneer",
            icon = painterResource(id = R.drawable.veneer)
        ),
        Category(
            name = "Implant",
            icon = painterResource(id = R.drawable.implant)
        )
    )

    Text(
        text = " Category",
        fontFamily = fontInria,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge
    )

    LazyRow{
        items(categories.size){
            CategoryCard(
                categories[it],
                navController,
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    navController: NavController
) {

    Column(
         modifier = Modifier
             .padding(top = 7.dp, bottom = 15.dp, end = 15.dp)
             .clip(RoundedCornerShape(10))
             .background(Indigo50)
             .padding(15.dp)
             .clickable {
                 navController.navigate(Screen.catagoryDoctors.withArgs(category.name))
             },
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
    ) {
         Image(
             modifier = Modifier.height(45.dp),
             painter = category.icon,
             contentDescription = "disease category"
         )
         Text(
             text = category.name,
             fontFamily = fontActor,
             textAlign = TextAlign.Center,
             fontSize = 12.sp
         )
    }
}
