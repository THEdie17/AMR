package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion del menú principal
 */
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Assigna valor a user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        
        //Assignamos los botones con su respectivo id del elemento
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val creditBtn = findViewById<Button>(R.id.credits)

        //Assignamos un estilo de letra a los textos de los botones
        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")
        loginBtn.typeface = tf
        registerBtn.typeface = tf
        creditBtn.typeface = tf

        //Cuando clicamos el botón nos llevara a la pantalla de Login
        loginBtn.setOnClickListener {
            Toast.makeText(this, "Click Login Button", Toast.LENGTH_LONG).show()
            jumpLogin()
        }

        //Cuando clicamos el botón nos llevara a la pantalla de Registrarse
        registerBtn.setOnClickListener {
            Toast.makeText(this, "Click Register Button", Toast.LENGTH_LONG).show()
            jumpRegister()
        }

        //Cuando clicamos el botón nos llevara a la pantalla de Creditos
        creditBtn.setOnClickListener {
            Toast.makeText(this, "Click Credits Button", Toast.LENGTH_LONG).show()
            jumpCredit()
        }
    }

    /**
     * Funcion que una vez clicado el botón nos dirige a la pantalla seleccionada
     */
    private fun jumpRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

    /**
     * Funcion que una vez clicado el botón nos dirige a la pantalla seleccionada
     */
    private fun jumpLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    /**
     * Funcion que una vez clicado el botón nos dirige a la pantalla seleccionada
     */
    private fun jumpCredit() {
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
    }

}
