package com.example.activemovereminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var loginbutton: Button
    private lateinit var registerbutton: Button
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginbutton = findViewById(R.id.loginButton)
        registerbutton = findViewById(R.id.registerButton)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")

        //assigna valor a user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

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
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun usuariLogejat() {
        if (user != null) {
            val intent = Intent(this, WeekMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}