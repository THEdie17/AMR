package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


@Suppress("DEPRECATION")
class Menu : AppCompatActivity() {
    //creem unes variables per comprovar ususari i authentificació
    private lateinit var closeSession: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var creditsBtn: Button
    private lateinit var scoresBtn: Button
    private lateinit var playBtn: Button
    private lateinit var profileBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var buttonmonday: ImageButton
    private lateinit var tuesdayButton: ImageButton
    private lateinit var WednsdayButton: ImageButton
    private lateinit var fridayButton: ImageButton
    private lateinit var saturdayButton: ImageButton
    private lateinit var pathing_user: String
    private lateinit var myScore: TextView
    private lateinit var NumRoutines: String
    private lateinit var NumExercise: String
    /*private lateinit var uid: TextView
    private lateinit var emailPlayer: TextView
    private lateinit var usernamePlayer: TextView*/
    private lateinit var uid: String
    private lateinit var emailPlayer: String
    private lateinit var usernamePlayer: String

    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        profileBtn = findViewById(R.id.buttonuser)
        backBtn = findViewById(R.id.buttomenu)

        //Rutinas
        buttonmonday = findViewById(R.id.buttonmonday)
        tuesdayButton = findViewById(R.id.tuesdayButton)
        WednsdayButton = findViewById(R.id.WednsdayButton)
        fridayButton = findViewById(R.id.fridayButton)
        saturdayButton = findViewById(R.id.saturdayButton)
        pathing_user = ""
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        val myRef = user?.uid?.let { database.reference.child("DATA_BASE_AMR").child(it) }
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val email = dataSnapshot.child("Email").getValue(String::class.java)
                val name = dataSnapshot.child("Name").getValue(String::class.java)
                val uidUser = dataSnapshot.child("Uid").getValue(String::class.java)
                val nRoutines = dataSnapshot.child("complete Routines").getValue(String::class.java)
                val nExercise = dataSnapshot.child("complete Exercise").getValue(String::class.java)
                // utilizar email y name según sea necesario
                if (uidUser != null) {
                    uid = uidUser
                }
                if (email != null) {
                    emailPlayer = email
                }
                if (name != null) {
                    usernamePlayer = name
                }
                if (nRoutines != null) {
                    NumRoutines= nRoutines
                }
                if (nExercise != null) {
                    NumExercise= nExercise
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Error al leer los valores.", error.toException())
            }
        })

        /*creditsBtn = findViewById(R.id.CreditsBtn)
        scoresBtn = findViewById(R.id.PuntuacionsBtn)
        playBtn = findViewById(R.id.jugarBtn)
        closeSession = findViewById(R.id.GoBack)

        editBtn.typeface = tf
        myScore.typeface = tf
        score.typeface = (tf)
        uid.typeface = (tf2)
        emailPlayer.typeface = (tf2)
        usernamePlayer.typeface = (tf2)

        //fem el mateix amb el text dels botons

        closeSession.typeface = (tf)
        creditsBtn.typeface = (tf)
        scoresBtn.typeface = (tf)
        playBtn.typeface = (tf)

        closeSession.setOnClickListener {
            closeTheSession()
        }

        creditsBtn.setOnClickListener {
            val intent = Intent(this, Credits::class.java)
            startActivity(intent)
        }


        scoresBtn.setOnClickListener {
            Toast.makeText(this, "Scores", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ScoreList::class.java)
            startActivity(intent)
        }
        */

        profileBtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)//Cambiar el destinio
            startActivity(intent)
        }
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        buttonmonday.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+user+"/z_Rutina Dilluns"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            startActivity(intent)
        }
        tuesdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+user+"/z_Rutina Dimarts"
            val intent = Intent(this, ScoreList::class.java)
            startActivity(intent)
        }
        WednsdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+user+"/z_Rutina Dimecres"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            startActivity(intent)
        }
        fridayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+user+"/z_Rutina Divendres"
            val intent = Intent(this, ScoreList::class.java)
            startActivity(intent)
        }
        saturdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+user+"/zz_Rutina Dissabte"
            val intent = Intent(this, ScoreList::class.java)
            startActivity(intent)
        }

    }
/*
    override fun onStart() {
        loggedUser()
        super.onStart()
    }

    private fun loggedUser() {
        if (user != null) {
            Toast.makeText(
                this, "Player logged in",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
*/
    private fun closeTheSession() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun getPathing():String {
        pathing_user = String()
        return pathing_user
    }


}
