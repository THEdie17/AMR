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
    private lateinit var pathing: String
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
            arrayOf("1","1","z1_Squats", "3","10","00:01:00","Sacar pecho, rodillas alineadas con la punta de los pies","of_sentadillas"),
            arrayOf("2","1","z2_Aixecament de genolls", "3","10xCostat","00:01:00","Subir la rodilla hasta la altura del maluc","of_rodillas_arriba"),
            arrayOf("3","1","z3_Pas endavant", "3","5xCostat","00:01:00","Mantenir el equlibri i començar amb distància curtes i anar ampliant la distància ", "of_zancadas"),
            arrayOf("4","1","z4_Aixecament amb bessons", "3","10","00:01:00","Mantenir el cos recte, i posarse de puntetes de manera controlada","of_gemelos"),
            arrayOf("5","1","z5_Abdominals", "3","5","00:01:00","Intentar portar el pit als genolls mantenint el abdomen fort","of_abdominales"),
            arrayOf("6","1","z6_Planxa", "3","30 Segons","00:01:00","Mantenir el cos paral·lel al terra sense ficar els genolls al terra.","of_plancha"),

            //Rutina Dimarts
            arrayOf("7","2","z1_Flexiones normales", "3","10","00:01:00","Mantenir les mans en linía amb les espatlles i mantenir els colze","of_flexiones_normales"),
            arrayOf("8","2","z2_Flexiones escalonadas", "3","5xCostat","00:01:00","Una mà més endavant de l'espatlla i l'altre a la mateixa linia que l'espatlla","of_flexiones_escalonadas"),
            arrayOf("9","2","z3_Flexiones V", "3","8","00:01:00","Fer una V amb el cos i flexionar els colzes","of_flexiones_v"),
            arrayOf("10","2","z4_Flexiones diamante", "3","5","00:01:00","Fer un triangle amb les mans i posarles al nivell de l'esternó","of_flexiones_diamante"),
            arrayOf("11","2","z5_Flexiones lumbares", "3","10","00:01:00","Aixecar pit i cames mantenint l'abdomen al terra","of_flexiones_lumbares"),
            arrayOf("12","2","z6_Fondos", "3","6","00:01:00","Les espatlles sempre per darrere el cap i quan baixes han de estar en un angle de 90º amb els colzes","of_flexiones_con_silla"),

            //Rutina Dimecres
            arrayOf("13","3","z1_Tissora braços", "3","30Segons","00:00:20","Braços paral·lels al terra","of_tijera_brazos"),
            arrayOf("14","3","z2_Rotació braços", "3","30Segons","00:00:30","Braços en linia amb les espatlles","of_elevacion_brazos"),
            arrayOf("15","3","z3_Estira braç i cama", "3","5xCostat","00:00:30","Braços, esquena i cames paral·lels al terra, braç contrari a la cama.","of_estiramiento"),
            arrayOf("16","3","z4_Remo", "3","10","00:01:00","Esquena recte i tirar els colzes cap enrere fins arribar a portar les mans a l'altura del maluc.","of_remo"),
            arrayOf("17","3","z5_Estirament lateral", "3","30SegonsXCostat","00:00:30","Girar el cos al costat contrari al que gires el cap","of_estiramiento_lateral"),
            arrayOf("18","3","z6_Postura vaca-gat", "3","30Segons","00:00:30","Esquena recta quan estires el coll una mica cap enrere, al baixar el cap pujar l'esquena.","of_estiramiento_espalda"),

            //Rutina Divendres
            arrayOf("19","3","z1_Correr", "1","1","01:30:00","Recomenable corre uns 8Km en Total","of_correr"),
            arrayOf("20","3","z2_Correr", "1","1","00:00:00","1,5 Km al trote lentament","of_correr"),
            arrayOf("21","3","z2_Correr", "1","1","00:00:00","1,5 Km al trote rapid","of_correr"),
            arrayOf("22","3","z4_Correr", "1","1","00:00:00","2 Km a màxima velocitat","of_correr"),
            arrayOf("23","3","z5_Correr", "1","1","00:00:00","2 Km al trote anant baixant la intensitat poc a poc","of_correr"),
            arrayOf("24","3","z6_Correr", "1","1","00:00:00","1 Km casi caminant","of_correr"),

            //Rutina Dissabte
            arrayOf("25","4","z1_Cops a l'aire", "1","30Segons","00:00:30","Fer un angle de 90º amb l'antebraç i l'espatlla, quan llences el cop el braç ha d'estar estirar completament","of_golpe_boxeo"),
            arrayOf("26","4","z2_Flexions profundes", "3","10","00:01:00","Quan baixes posar tot el cos en contacte amb el terra, mans en linia amb les espatlles","of_flexiones_intensivos"),
            arrayOf("27","4","z2_Escalara montanya", "3","5xCostat","00:01:00","Portar els genolls fins l'altura del pit","of_mountain_climbing"),
            arrayOf("28","4","z4_Burpies", "3","10","00:01:00","Controlar la respiració i seguir els consells anteriors","of_burpies"),
            arrayOf("29","4","z5_Aixecar cames", "3","10","00:01:00","Amb tot el cos estirat, aixecar les cames fins arribar a l'altura de la cintura","of_abdominales_bajos"),
            arrayOf("30","4","z6_Planxa", "1","1 Minut","00:00:20","Mantenir el cos paral·lel al terra sense ficar els genolls al terra.","of_plancha")
        )
        //Quan es crea un usuari nou, se'ls crea les rutines per cada usuari amb els exercicis
        if (user != null) {
            var num_exe = 0
            for (i in 0 until arrayRoutines.size) {
                val id_Routine = arrayRoutines[i][0]
                val nameRoutine: String = arrayRoutines[i][1]
                val weekDay = arrayRoutines[i][2]
                val objective: String = arrayRoutines[i][3]
                val complete: Boolean = false

                val dadesJugador : HashMap<String,String> = HashMap<String, String>()

                //dadesJugador["ID_Routine"] = id_Routine
                //dadesJugador["nameRoutine"] = nameRoutine
                //dadesJugador["weekDay"] = weekDay
                //dadesJugador["objective"] = objective
                dadesJugador["Uid"] = user
                //dadesJugador["complete"] = complete.toString()
                // Creem un punter a la base de dades i li donem un nom
                val reference: DatabaseReference = database.getReference("DATA_BASE_AMR/"+user)
                if(reference!=null) {
                    //crea un fill amb els valors de dadesJugador
                    reference.child("z"+nameRoutine).setValue(dadesJugador)

                    for (j in 1..6) {
                        val id_exercise = arrayExercise[num_exe][0]
                        val id_Routine = arrayExercise[num_exe][1]
                        val nameExercise: String = arrayExercise[num_exe][2]
                        val nSeries: String = arrayExercise[num_exe][3]
                        val nReplays: String = arrayExercise[num_exe][4]
                        val rest: String = arrayExercise[num_exe][5]
                        val suggestion: String = arrayExercise[num_exe][6]
                        val imgName = arrayExercise[num_exe][7]
                        val complete: Boolean = false

                        val dadesRutines : HashMap<String,String> = HashMap<String, String>()
                        dadesRutines["ID_Exercise"] = id_exercise
                        dadesRutines["ID_Routine"] = id_Routine
                        dadesRutines["nameExercise"] = nameExercise
                        dadesRutines["nSeries"] = nSeries
                        dadesRutines["nReplays"] = nReplays
                        dadesRutines["rest"] = rest
                        dadesRutines["suggestion"] = suggestion
                        dadesRutines["imgName"] = imgName
                        dadesRutines["complete"] = complete.toString()
                        // Creem un punter a la base de dades i li donem un nom
                        val reference2: DatabaseReference = database.getReference("DATA_BASE_AMR/"+user+"/z" + nameRoutine)
                        if(reference2!=null) {
                            //crea un fill amb els valors de dadesJugador
                            reference2.child(nameExercise).setValue(dadesRutines)
                        }
                        num_exe = num_exe + 1
                        pathing = "DATA_BASE_AMR/"+user+"/z" + nameRoutine
                    }
                }
            }
        }
    }
}

