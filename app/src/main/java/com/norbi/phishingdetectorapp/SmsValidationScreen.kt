package com.norbi.phishingdetectorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norbi.phishingdetectorapp.viewmodel.MainViewModel


@Composable
fun SmsValidationScreen(
    vm2: MainViewModel,
    onBack: () -> Unit
) {
    var inputSms by remember { mutableStateOf("") }
    val result by vm2.smsResult.observeAsState()
    val error  by vm2.smsError.observeAsState()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "SMS Validation",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        OutlinedTextField(
            value = inputSms,
            onValueChange = { inputSms = it },
            label = { Text("Treść SMS") },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (inputSms.isNotBlank()) {
                    vm2.classifySms(inputSms)
                    showDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Sprawdź SMS", fontSize = 16.sp)
        }
    }

    // --- AlertDialog z wynikiem ---
    if (showDialog && result != null) {
        val res = result!!
        val isSpam = res.label.equals("spam", ignoreCase = true)

        AlertDialog(
            onDismissRequest = {
                showDialog = false
                vm2.clearSmsResult()
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = if (isSpam) "Fałszywy sms" else "Sms Wiarygodny",
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = if (isSpam) Color.Red else Color(0xFF2E7D32),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pewność: ${"%.1f".format(res.confidence * 100)}%",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = if (isSpam) Color.Red else Color(0xFF2E7D32),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    vm2.clearSmsResult()
                }) {
                    Text(
                        "Zamknij",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
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

        )
    }
}
