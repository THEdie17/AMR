package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEt = findViewById(R.id.emailEt)
        passEt = findViewById(R.id.passEt)
        nameEt = findViewById(R.id.nameEt)
        register = findViewById(R.id.register)
        auth = FirebaseAuth.getInstance()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance() //or use getDateInstance()
        val formattedDate = formatter.format(date)

        val tf = Typeface.createFromAsset(assets,"fonts/edosz.ttf")

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
            val uidString: String = user.uid
            val emailString: String = emailEt.text.toString()
            val passString: String = passEt.text.toString()
            val usernameString: String = nameEt.text.toString()
            val completeRoutines = 0
            val completeExercise = 0

            val dadesJugador : HashMap<String,String> = HashMap<String, String>()

            dadesJugador["Uid"] = uidString
            dadesJugador["Email"] = emailString
            dadesJugador["Password"] = passString
            dadesJugador["Name"] = usernameString
            dadesJugador["complete Routines"] = completeRoutines.toString()
            dadesJugador["complete Exercise"] = completeExercise.toString()
            // Creem un punter a la base de dades i li donem un nom
            val reference: DatabaseReference = database.getReference("DATA_BASE_AMR")

            if(reference!=null) {
                //crea un fill amb els valors de dadesJugador
                reference.child(uidString).setValue(dadesJugador)
                makeRoutines(uidString)
            }
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(
                this, "ERROR CREATE USER ",Toast.LENGTH_SHORT).show()
        }
    private fun makeRoutines(user: String){

        val arrayRoutines = Array(5) { Array(4) { "0" } }
        arrayRoutines[0][0] = "1"
        arrayRoutines[0][1] = "_Rutina Dilluns"
        arrayRoutines[0][2] = "1"
        arrayRoutines[0][3] = "Part Inferior"

        arrayRoutines[1][0] = "2"
        arrayRoutines[1][1] = "_Rutina Dimarts"
        arrayRoutines[1][2] = "2"
        arrayRoutines[1][3] = "Pit, Hombros i Triceps"

        arrayRoutines[2][0] = "3"
        arrayRoutines[2][1] = "_Rutina Dimecres"
        arrayRoutines[2][2] = "3"
        arrayRoutines[2][3] = "Esquena i Biceps"

        arrayRoutines[3][0] = "5"
        arrayRoutines[3][1] = "_Rutina Divendres"
        arrayRoutines[3][2] = "5"
        arrayRoutines[3][3] = "Resistencia"

        arrayRoutines[4][0] = "6"
        arrayRoutines[4][1] = "z_Rutina Dissabte"
        arrayRoutines[4][2] = "6"
        arrayRoutines[4][3] = "Tot el cos"


        if (user != null) {
            for (i in 0 until arrayRoutines.size) {
                val id_Routine = arrayRoutines[i][0]
                val nameRoutine: String = arrayRoutines[i][1]
                val weekDay = arrayRoutines[i][2]
                val objective: String = arrayRoutines[i][3]
                val complete: Boolean = false

                val dadesJugador : HashMap<String,String> = HashMap<String, String>()

                dadesJugador["ID_Routine"] = id_Routine
                dadesJugador["nameRoutine"] = nameRoutine
                dadesJugador["weekDay"] = weekDay
                dadesJugador["objective"] = objective
                dadesJugador["Uid"] = user
                dadesJugador["complete"] = complete.toString()
                // Creem un punter a la base de dades i li donem un nom
                val reference: DatabaseReference = database.getReference("DATA_BASE_AMR/"+user)
                if(reference!=null) {
                    //crea un fill amb els valors de dadesJugador
                    reference.child("z"+nameRoutine).setValue(dadesJugador)
                }
            }

        }

    }
}

