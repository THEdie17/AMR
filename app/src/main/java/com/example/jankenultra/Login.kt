package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {
    companion object{
        var user_email: String? = null
    }
    //Les variables que necessitem per logejar el usuari
    private lateinit var emailLogin : EditText
    private lateinit var passLogin : EditText
    private lateinit var login : Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //Despleguem les variables que farem servir
        emailLogin = findViewById(R.id.emailLogin)
        passLogin = findViewById(R.id.passLogin)
        auth = FirebaseAuth.getInstance()
        login = findViewById(R.id.login)

        //Aquesta variable tf ens serveix per poder cambiar el estil de la lletra que es veurà
        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")
        login.typeface = (tf)

        //Aquest fragment és per validar que tant el email i la password introduïdes són correctes 
        login.setOnClickListener {
            //Abans de fer el registre validem les dades
            val email: String = emailLogin.text.toString()
            val passw: String = passLogin.text.toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLogin.error = "Invalid Mail"
            } else if (passw.length < 6) {//Validem que el tamany de la password sigui correcte
                passLogin.error = "Password less than 6 chars"
            } else {
                // aquí farem LOGIN al jugador
                playerLogin(email, passw)
            }
        }
    }
//Comprovar que les dades de email i password són correctes i que abans ja han siguit registrades
    private fun playerLogin(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    val tx = "Benvingut $email"
                    Toast.makeText(this, tx, Toast.LENGTH_LONG).show()
                    auth.currentUser
                    user_email = email
                    updateUI()
                } else {
                    Toast.makeText(
                        this, "ERROR Autentificació",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    //Aquesta funció ens portarà si tot es correccte i apretem el botó de login a la pantalla de les rutines
    private fun updateUI() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}
