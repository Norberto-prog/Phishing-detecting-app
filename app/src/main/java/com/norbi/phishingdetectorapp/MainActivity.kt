package com.norbi.phishingdetectorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.norbi.phishingdetectorapp.ui.theme.PhishingDetectorAppTheme
import com.norbi.phishingdetectorapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhishingDetectorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Menu) }
                    when (currentScreen) {
                        Screen.Menu -> MenuScreen(
                            onCheckContent = { currentScreen = Screen.CheckContent },
                            onCheckSms   = { currentScreen = Screen.CheckSms },
                            onCheckPhone   = { currentScreen = Screen.CheckPhone }
                        )
                        Screen.CheckContent -> PhishingScreen(vm) {
                            currentScreen = Screen.Menu
                        }
                        Screen.CheckSms -> SmsValidationScreen(
                            vm2 = vm,
                            onBack = { currentScreen = Screen.Menu }
                        )
                        Screen.CheckPhone   -> PhoneValidationScreen(onBack = { currentScreen = Screen.Menu })
                    }
                }
            }
        }
    }

    
}

@Composable
fun PhishingScreen(vm: MainViewModel, onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val result by vm.result.observeAsState()
    val error  by vm.error.observeAsState()

    var showResultDialog by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        if (result != null) {
            showResultDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "SpamCheck",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )}
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(48.dp)
            )  {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }


        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Temat: Prośba o spotkanie\n" +
                    "Dzień dobry Panie Kowalski,\n" +
                    "czy możemy się spotkać w przyszłym tygodniu, aby omówić raport kwartalny?  \n" +
                    "Czy środa o 10:00 w Pana biurze będzie odpowiednia?\n" +
                    "\n" +
                    "Pozdrawiam serdecznie,  \n" +
                    "Anna Nowak  \n" +
                    "Tel. 600-123-456\n",
            fontSize = 8.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Spam czy nie sprawdź sam!",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))


        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Treść e-maila") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                if (input.isNotBlank()) {
                    vm.classifyEmail(input)
                    showResultDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Sprawdź",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        if (showResultDialog && result != null) {
            val res = result!!
            val isSpam = res.label.lowercase() == "spam"

            AlertDialog(
                onDismissRequest = {
                    showResultDialog = false
                    vm.clearResult()
                },
                title = {
                    Text(
                        text = if (isSpam) "Ostrzeżenie" else "Wynik",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isSpam) "Wiadomość fałszywa" else "Wiadomość prawdziwa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isSpam) Color.Red else Color(0xFF2E7D32),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pewność: ${"%.1f".format(res.confidence * 100)}%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (isSpam) Color.Red else Color(0xFF2E7D32),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showResultDialog = false
                        vm.clearResult()
                    }) {
                        Text("Zamknij", fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color(0xFFF9F9F9)
            )
        }
        error?.let { err ->
            Text(
                text = "Błąd: $err",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}