package com.frontlinehelper.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a simple text view for now
        val textView = TextView(this)
        textView.text = "Frontline Helper - Main"
        textView.textSize = 18f
        textView.setPadding(50, 50, 50, 50)
        
        setContentView(textView)
    }
}
