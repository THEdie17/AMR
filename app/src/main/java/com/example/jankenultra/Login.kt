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

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion de cuando inicias session con tus datos
 */
class Login : AppCompatActivity() {
    companion object{
        var user_email: String? = null
    }

    //Les variables que necessitamos para inicar sessión con el usuario creado
    private lateinit var emailLogin : EditText
    private lateinit var passLogin : EditText
    private lateinit var login : Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        // Desplegamos las variables que utilizaremos
        emailLogin = findViewById(R.id.emailLogin)
        passLogin = findViewById(R.id.passLogin)
        auth = FirebaseAuth.getInstance()
        login = findViewById(R.id.login)


        //Esta variable tf nos sirve para cambiar el estilo de la letra
        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")
        login.typeface = (tf)

        //Este fragmento és para validar que tanto el gmail y la password introducidas són correctas
        login.setOnClickListener {
            //Antes de hacer el registro comprobamos que sean correctas
            val email: String = emailLogin.text.toString()
            val passw: String = passLogin.text.toString()
            // validación del correo
            // si no es de tipus correo
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLogin.error = "Invalid Mail"
            } else if (passw.length < 6) {//Validamos que el tamaño de la password sea correcto
                passLogin.error = "Password less than 6 chars"
            } else {
                // Aquí haremos LOGIN al jugador
                playerLogin(email, passw)
            }
        }
    }
    /**
     * Funcion para comprovar que les dades de gmail y password són correctas y que antes hayan sido registradas
     * @param email email que el usuario  utiliza
     * @param passw password del usuario
     */
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

    /**
     *  Esta función nos llevarà a la página de las rutinas una vez la comprovacion de los datos sea correcto
     */
    private fun updateUI() {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}
