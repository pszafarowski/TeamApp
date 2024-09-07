package com.example.TeamApp.profile

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.R
import com.example.TeamApp.data.User
import com.example.TeamApp.excludedUI.UserProfileButton
import com.example.TeamApp.excludedUI.Variables
import com.example.compose.TeamAppTheme



fun getIconResourceId(context: Context, iconName: String): Int {
    val resourceId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    return resourceId
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, user: User) {
    val context = LocalContext.current
    val iconId = getIconResourceId(context, user.avatar!!)

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    val viewModel: ProfileViewModel = viewModel()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.linearGradient(colors = gradientColors))
        .padding(start = 6.dp, end = 6.dp, top = 28.dp, bottom = 72.dp)
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(iconId),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .size(160.dp)
                )
                Text(
                    text = "Witaj ${user.name}!",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                        fontWeight = FontWeight(700),
                        color = Variables.Black,
                        textAlign = TextAlign.Center,
                    )
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2F2F2)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Edytuj profil",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                            fontWeight = FontWeight(600),
                            color = Variables.Black,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                        .background(
                            Color(0xFFF2F2F2),
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ){
                        //UserProfileButton(iconId = R.drawable.mapicon, mainText = "Twoje wydarzenia", bottomText = "Zarządzaj swoimi wydarzeniami")
                        UserProfileButton(iconId = R.drawable.settingsicon, mainText = "Ustawienia", bottomText = "Ustawienia konta",navController, "settings")
                        //UserProfileButton(iconId = R.drawable.contactusicon, mainText = "Napisz do nas", bottomText = "Skontaktuj się z nami")
                        //UserProfileButton(iconId = R.drawable.shareicon, mainText = "Udostępnij", bottomText = "Niech wszyscy wiedzą")
                    }
                }
            }
        }
    }
}

