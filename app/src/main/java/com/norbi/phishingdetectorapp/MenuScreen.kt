package com.norbi.phishingdetectorapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

@Composable
fun MenuScreen(
    onCheckContent: () -> Unit,
    onCheckSms: () -> Unit,
    onCheckPhone: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                    text = "SpamCheck",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(
                    "Sprawdź treść e-maila" to onCheckContent,
                    "Sprawdź treść sms" to onCheckSms,
                    "Sprawdź nr telefonu" to onCheckPhone
                ).forEach { (title, action) ->
                    Button(
                        onClick = action,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(64.dp)
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        AnimatedTypingText(
            text = "Phishing czy jednak nie?\n" +
                    "Sprawdź sam!",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        )
    }
}
@Composable
fun AnimatedTypingText(
    text: String,
    modifier: Modifier = Modifier,
    typingDelay: Long = 100L,
    pauseDelay: Long = 1000L
) {
    var displayText by remember { mutableStateOf("") }
    var index by remember { mutableIntStateOf(0) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(text) {
        while (true) {
            if (!isDeleting) {
                if (index < text.length) {
                    displayText = text.substring(0, index)
                    index++
                    delay(typingDelay)
                } else {
                    delay(pauseDelay)
                    isDeleting = true
                }
            } else {
                if (index > 0) {
                    displayText = text.substring(0, index)
                    index--
                    delay(typingDelay)
                } else {
                    isDeleting = false
                    index = 0
                }
            }
        }
    }

    Text(
        text = displayText,
        fontSize = 16.sp,
        color = Color.White,
        modifier = modifier
    )
}
