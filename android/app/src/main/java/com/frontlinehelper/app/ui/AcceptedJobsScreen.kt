package com.frontlinehelper.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AcceptedJobsScreen() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Accepted Jobs", style = MaterialTheme.typography.h6)
            // list accepted jobs here
        }
    }
}
