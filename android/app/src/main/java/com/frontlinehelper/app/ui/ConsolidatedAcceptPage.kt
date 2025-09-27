package com.frontlinehelper.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConsolidatedAcceptPage() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Consolidated Accept Page (Accept All UX)", style = MaterialTheme.typography.h6)
            // rows of jobs and Open Accept Page buttons would go here
            Button(onClick = { /* open all in webview */ }) {
                Text("Open All in Webview")
            }
        }
    }
}
