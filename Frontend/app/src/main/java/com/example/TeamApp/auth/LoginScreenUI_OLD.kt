package com.example.TeamApp.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TeamApp.R
import kotlinx.coroutines.delay

@Composable
fun LoginScreen_OLD() {
    val viewModel: LoginViewModel = viewModel()
    var showSnackbar by remember { mutableStateOf(false) }
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    val emailSent by viewModel.emailSent.observeAsState()
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(loginSuccess, registerSuccess, emailSent) {
        when {
            emailSent != null -> {
                snackbarMessage = "Email"
                snackbarSuccess = emailSent ?: false
                showSnackbar = true
            }
            loginSuccess != null -> {
                snackbarMessage = "login"
                snackbarSuccess = loginSuccess ?: false
                showSnackbar = true
            }
            registerSuccess != null -> {
                snackbarMessage = "register"
                snackbarSuccess = registerSuccess ?: false
                showSnackbar = true
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(27.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextComponentUpper1(value = "")
            TextComponentUpper2(value = "Welcome back")
            Spacer(modifier = Modifier.height(23.dp))
            MyTextField(labelValue = "E-mail", painterResource(id = R.drawable.emailicon))
            Spacer(modifier = Modifier.height(12.dp))
            PasswordTextField(labelValue = "Password", painterResource(id = R.drawable.passwordicon))
            Spacer(modifier = Modifier.height(31.dp))
            TextComponentUnderLined(value = "Forgot your password?") {
                val intent : Intent = Intent(context, ForgotPasswordActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
            Spacer(modifier = Modifier.height(15.dp))
            ButtonComponent(value = "Login")
            Spacer(modifier = Modifier.height(21.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(21.dp))
            Row(
                modifier = Modifier
                    .padding(21.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                FaceBookButton()
                Spacer(modifier = Modifier.width(45.dp))
                GoogleButton()
            }
            Spacer(modifier = Modifier.height(21.dp))
            ClickableRegisterComponent(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    if (showSnackbar) {
        CustomSnackbar(success = snackbarSuccess, type = snackbarMessage) {
            showSnackbar = false
            viewModel.resetSuccess()  // Reset the success states after snackbar is dismissed
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview_OLD() {
    LoginScreen()
}

@Composable
fun TextComponentUnderLined(value: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = "forgot_password")
        withStyle(style = SpanStyle(color = Color.Gray, textDecoration = TextDecoration.Underline)) {
            append(value)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .wrapContentSize(Alignment.Center),  // This centers the text
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let {
                onClick()
            }
        }
    )
}



@Composable
fun FaceBookButton() {
    val viewModel: LoginViewModel = viewModel()
    // Replace with your image resource ID
    val image: Painter = painterResource(id = R.drawable.facebooklogo)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(60.dp) // Adjusted size of the button
            .clip(RoundedCornerShape(14.dp)) // Rounded corners with 12 dp radius
            .background(Color.White)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            ) // Border with color and shape
            .clickable { viewModel.signInWithFacebook(context) } // Clickable functionality
            .padding(8.dp) // Padding inside the button
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GoogleButton() {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    val image: Painter = painterResource(id = R.drawable.googlelogo)

    Box(
        modifier = Modifier
            .size(60.dp) // Size of the button
            .clip(RoundedCornerShape(14.dp)) // Rounded corners with 24 dp radius
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(14.dp)
            ) // Border with color and shape
            .clickable { viewModel.signInWithGoogle(context) } // Clickable functionality
            .background(Color.White)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CustomSnackbar(success: Boolean, type: String, onDismiss: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onDismiss()
        LoginViewModel().resetSuccess()
    }

    Snackbar(
        modifier = Modifier
            .padding(80.dp)
            .wrapContentSize(Alignment.Center),
        shape = RoundedCornerShape(60.dp),
        containerColor = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
        contentColor = Color.White,
        action = {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (success) {
                    when (type) {
                        "login" -> "Login Successful"
                        "Email" -> "Email Sent"
                        else -> "Registration Successful"
                    }
                } else {
                    when (type) {
                        "login" -> "Login Failed"
                        "Email" -> "Email not registered"
                        else -> "Registration Failed"
                    }
                },
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }
    }
}