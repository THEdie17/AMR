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
//Declarem les variables que farem servir
    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var nameEt: EditText
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Les dades a introduïr
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

//Para registrar el email i el password
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
        //Si el usuari no existeix quan es crea, les dades que s'han de introduïr
        if (user != null) {
            val uidString: String = user.uid
            val emailString: String = emailEt.text.toString()
            val passString: String = passEt.text.toString()
            val usernameString: String = nameEt.text.toString()
            val completeRoutines = 0
            val completeExercise = 0

            val dadesJugador : HashMap<String,String> = HashMap<String, String>()
            //Dades que es guarden a la base de dades quan el jugador es registra
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


    //Creem les rutines dintre del Array
    private fun makeRoutines(user: String){
        val arrayRoutines: Array<Array<String>> = arrayOf(
            //      Id - Nom rutina - Dia - Objectiu - Complert = false
            arrayOf("1", "_Rutina Dilluns","1","Part Inferior"),
            arrayOf("2", "_Rutina Dimarts","2","Pit, Hombros i Triceps"),
            arrayOf("3", "_Rutina Dimecres","3","Esquena i Biceps"),
            arrayOf("5", "_Rutina Divendres","5","Resistencia"),
            arrayOf("6", "z_Rutina Dissabte","6","Tot el cos")
        )
        val arrayExercise: Array<Array<String>> = arrayOf(
            //      Id - Rutina - Nom - Series - Repeticions - Descans - Consell - Imatge(URL) - Complert = false
            //Rutina Dilluns
            arrayOf("1","1","Squats", "3","10","00:01:00","","RutinaDilluns/OF_Sentadillas","false"),
            arrayOf("2","1","Aixecament de genolls", "3","10xCostat","00:01:00","","RutinaDilluns/OF_Rodillas arriba","false"),
            arrayOf("3","1","Pas endavant", "3","5xCostat","00:01:00","","RutinaDilluns/OF_Zancadas","false"),
            arrayOf("4","1","aixecament amb bessons", "3","10","00:01:00","","RutinaDilluns/OF_gemelos","false"),
            arrayOf("5","1","Abdominals", "3","5","00:01:00","","RutinaDilluns/OF_Abdominales","false"),
            arrayOf("6","1","planxa", "3","30 Segons","00:01:00","","RutinaDilluns/OF_Plancha","false"),

            //Rutina Dimarts
            arrayOf("7","2","Flexiones normales", "3","10","00:01:00","","RutinaDimarts/OF_Flexiones normales","false"),
            arrayOf("8","2","Flexiones escalonadas", "3","5xCostat","00:01:00","","RutinaDimarts/OF_Flexiones escalonadas","false"),
            arrayOf("9","2","Flexiones V", "3","8","00:01:00","","RutinaDimarts/OF_Flexiones v","false"),
            arrayOf("10","2","Flexiones diamante", "3","5","00:01:00","","RutinaDimarts/OF_Flexiones diamante","false"),
            arrayOf("11","2","Flexiones lumbares", "3","10","00:01:00","","RutinaDimarts/OF_Flexiones lumbares","false"),
            arrayOf("12","2","Fondos", "3","6","00:01:00","","RutinaDimarts/OF_Flexiones con silla","false"),

            //Rutina Dimecres
            arrayOf("13","3","Tissora braços", "3","30Segons","00:00:20","","RutinaDimecres/OF_Tijera brazos","false"),
            arrayOf("14","3","Rotació braços", "3","30Segons","00:00:30","","RutinaDimecres/OF_elevacion brazos","false"),
            arrayOf("15","3","Estira braç i cama", "3","5xCostat","00:00:30","","RutinaDimecres/OF_estiramiento","false"),
            arrayOf("16","3","Remo", "3","10","00:01:00","","RutinaDimecres/OF_remo","false"),
            arrayOf("17","3","Estirament lateral", "3","30SegonsXCostat","00:00:30","","RutinaDimecres/OF_Estiramiento lateral","false"),
            arrayOf("18","3","Postura vaca-gat", "3","30Segons","00:00:30","","RutinaDimecres/OF_Estiramiento espalda","false"),

            //Rutina Divendres
            arrayOf("19","3","Correr", "1","1","00:00:00","Recomenable corre uns 10Km","RutinaDivendres/OF_Sentadillas","false"),

            //Rutina Dissabte
            arrayOf("20","4","Cops a l'aire", "1","30Segons","00:00:30","","RutinaDivendres/OF_golpe boxeo","false"),
            arrayOf("21","4","Flexions profundes", "3","10","00:01:00","","RutinaDivendres/OF_flexiones intensivos","false"),
            arrayOf("22","4","Escalara montanya", "3","5xCostat","00:01:00","","RutinaDivendres/OF_mountain climbing","false"),
            arrayOf("23","4","Burpies", "3","10","00:01:00","","RutinaDivendres/OF_burpies","false"),
            arrayOf("24","4","Aixecar cames", "3","10","00:01:00","","RutinaDivendres/OF_Abdominales bajos","false"),
            arrayOf("25","4","Planxa", "1","1Minut","","","RutinaDivendres/OF_plancha","false")
        )
        //Quan es crea un usuari nou, se'ls crea les rutines per cada usuari amb els exercicis
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

    //Creem els excersisi dintre de les rutines
    private fun makeExercise(user: String){

        val arrayRoutines: Array<Array<String>> = arrayOf(
            arrayOf("1", "_Rutina Dilluns","1","Part Inferior"),
            arrayOf("2", "_Rutina Dimarts","2","Pit, Hombros i Triceps"),
            arrayOf("3", "_Rutina Dimecres","3","Esquena i Biceps"),
            arrayOf("5", "_Rutina Divendres","5","Resistencia"),
            arrayOf("6", "z_Rutina Dissabte","6","Tot el cos")
        )
    }
}

