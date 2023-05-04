package com.example.activemovereminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var loginbutton: Button
    private lateinit var registerbutton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginbutton = findViewById(R.id.loginButton)
        registerbutton = findViewById(R.id.registerButton)

        loginbutton.setOnClickListener {
            //va a la pantalla Login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        registerbutton.setOnClickListener {
            //va a la pantalla Login
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}