package com.frontlinehelper.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(userDisplayName: String) {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "$userDisplayName - ${java.time.LocalDate.now()}", style = MaterialTheme.typography.subtitle2)
            // ... quick view cards would go here
            Button(onClick = { /* navigate to consolidated accept page */ }) {
                Text("Open Consolidated Accept Page")
            }
        }
    }
}
