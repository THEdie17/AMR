package com.example.activemovereminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    //Creem els 2 botons necessaris que ens portaran a la següent pàgina
    private lateinit var loginbutton: Button
    private lateinit var registerbutton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginbutton = findViewById(R.id.loginButton)
        registerbutton = findViewById(R.id.registerButton)

        //On end dirigeix quan clickem el botó de Login
        loginbutton.setOnClickListener {
            //va a la pantalla Login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }//LoginButton 
        
        //On end dirigeix quan clickem el botó de Sign Up
        registerbutton.setOnClickListener {
            //va a la pantalla Login
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }//RegisterButton
        
    }//fun onCreate()
}//MainActivity
