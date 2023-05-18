package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //assigna valor a user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        
        //Assignem els botons de login y sign up
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val creditBtn = findViewById<Button>(R.id.credits)

        //Assignem el estil de lletra als botons
        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")
        loginBtn.typeface = tf
        registerBtn.typeface = tf
        creditBtn.typeface = tf

        //Quan cliquem el botó de login et portarà a la pantalla de login
        loginBtn.setOnClickListener {
            Toast.makeText(this, "Click Login Button", Toast.LENGTH_LONG).show()
            jumpLogin()
        }
        
        //Quan cliquem el botó de registrar el teu usuari et portarà a la pantalla de registrar-se
        registerBtn.setOnClickListener {
            Toast.makeText(this, "Click Register Button", Toast.LENGTH_LONG).show()
            jumpRegister()
        }

        creditBtn.setOnClickListener {
            Toast.makeText(this, "Click Credits Button", Toast.LENGTH_LONG).show()
            jumpCredit()
        }
    }

    //Aquesta es la funció que fem servir per canviar de pantalla del Main al registre
    private fun jumpRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

     //Aquesta es la funció que fem servir per canviar de pantalla del Main al login
    private fun jumpLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun jumpCredit() {
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
    }

/*  override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
//Si l'usuari ja esta logejat et portarà a la pantalla de les rutines.
    private fun usuariLogejat() {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }*/
}
