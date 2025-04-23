package com.example.doctors_appointment.ui.patientsUI.mainHome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doctors_appointment.R
import com.example.doctors_appointment.ui.theme.Indigo400

// Featured Item

data class Featured(
    val name: String,
    val motivation: String,
    //val specialization: String,
    val image: Painter
)


@Composable
fun FeaturedItems() {

    val featuredItems = listOf(

        Featured(
            name = "Khanh Linh",
            motivation = "A senior heart Specialist. Care your Heart with the best doctor in the town",
            image = painterResource(id = R.drawable.senior_doctor)
        ),
        Featured(
            name = "Khanh Long",
            motivation = "A potol Specialist. Potol is so neutricious. It is good for health",
            image = painterResource(id = R.drawable.dr1)
        ),
        Featured(
            name = "Lan Nhi",
            motivation = "An experienced gynocolgist. Care mothers and their child. They makes the future of the world",
            image = painterResource(id = R.drawable.female_doctor)
        ),
        Featured(
            name = "Thai Lam",
            motivation = "A child Specialist. foitrotg rojoihbgorgjoigoj",
            image = painterResource(id = R.drawable.salina_zaman)
        )
    )

    Text(
        text = " Find Your",
        fontFamily = fontActor,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = "  Specialist",
        fontFamily = fontInria,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        style = MaterialTheme.typography.labelLarge
    )

    LazyRow{
        items(featuredItems.size){
            FeatureCard(featuredItems[it])
        }
    }
}

@Composable
fun FeatureCard(
    featured: Featured
) {

    Box(
        modifier = Modifier
            .height(180.dp)
            .width(320.dp)
            .padding(end = 15.dp, bottom = 9.dp, top = 7.dp)
            .clip(RoundedCornerShape(10))
            .background(Indigo400)
//            .border(2.dp, Indigo500, RoundedCornerShape(10))
    ){
        Box(
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(10))
                .padding(top = 5.dp),
        ){
            Image(
                painter = featured.image,
                contentDescription = "featured image",
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.BottomEnd,
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp, top = 40.dp, end = 80.dp)
        ) {
            Text(
                text = "Dr. ${featured.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontFamily = fontInria,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = featured.motivation,
                color = Color.White,
                fontFamily = fontInria,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 5.dp, end = 50.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
