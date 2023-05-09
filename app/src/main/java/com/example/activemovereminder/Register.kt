package com.example.activemovereminder

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
class Register : AppCompatActivity() {

    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nameEt: EditText
    private lateinit var dateTxt: TextView
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        emailEt = findViewById(R.id.nameEt)
        passEt = findViewById(R.id.emailEt)
        nameEt = findViewById(R.id.passwdEt)
        dateTxt = findViewById(R.id.dateTxt)
        register = findViewById(R.id.registButton)
        auth = FirebaseAuth.getInstance()


        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formattedDate = formatter.format(date)
        //ara la mostrem al TextView
        dateTxt.text = formattedDate

        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")

        dateTxt.typeface = tf
        register.typeface = tf

        register.setOnClickListener {
            //Abans de fer el registre validem les dades
            val email: String = emailEt.text.toString()
            val pass: String = passEt.text.toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEt.error = "Invalid Mail"
            } else if (pass.length < 6) {
                passEt.error = "Password less than 6 chars"
            } else {
                registerPlayer(email, pass)
            }

        }
    }


    private fun registerPlayer(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed .", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) = //hi ha un interrogant perquè podria ser null
        if (user != null) {
            val score = 0
            val uidString: String = user.uid
            val emailString: String = emailEt.text.toString()
            val passString: String = passEt.text.toString()
            val usernameString: String = nameEt.text.toString()
            val dateString: String = dateTxt.text.toString()

            val dadesJugador : HashMap<String,String> = HashMap<String, String>()

            dadesJugador["Uid"] = uidString
            dadesJugador["Email"] = emailString
            dadesJugador["Password"] = passString
            dadesJugador["Nom"] = usernameString
            dadesJugador["Data"] = dateString
            dadesJugador["Puntuacio"] = score.toString()
            // Creem un punter a la base de dades i li donem un nom
            val database: FirebaseDatabase =FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
            val reference: DatabaseReference = database.getReference("DATA_BASE_AMR")
            if(reference!=null) {
                //crea un fill amb els valors de dadesJugador
                reference.child(uidString).setValue(dadesJugador)
            }else{

            }
        } else {
            Toast.makeText(
                this, "ERROR CREATE USER ",Toast.LENGTH_SHORT).show()
        }
}

