package com.example.jankenultra

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion del perfil del cliente
 */
class EditProfile : AppCompatActivity() {

    /**
     * Variables y elementos que utilizamos en esta pantalla
     */
    private lateinit var back: Button
    private lateinit var logOut: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailPlayer: TextView
    private lateinit var usernamePlayer: TextView
    private lateinit var numRoutines: TextView
    private lateinit var numExercise: TextView
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val tf = Typeface.createFromAsset(assets, "fonts/edosz.ttf")
        val tf2 = Typeface.createFromAsset(assets, "fonts/retro.ttf")

        usernamePlayer = findViewById(R.id.nameUser)
        emailPlayer = findViewById(R.id.useEmail)
        numExercise = findViewById(R.id.nExercisis)
        numRoutines = findViewById(R.id.nRutines)

        logOut = findViewById(R.id.LogOut)
        logOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        back = findViewById(R.id.GoBack)
        back.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        back.typeface = tf

        emailPlayer.typeface = (tf2)
        usernamePlayer.typeface = (tf2)
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        val myRef = user?.uid?.let { database.reference.child("DATA_BASE_AMR").child(it) }
        myRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val email = dataSnapshot.child("Email").getValue(String::class.java)
                val name = dataSnapshot.child("Name").getValue(String::class.java)
                val nRoutines = dataSnapshot.child("complete Routines").getValue(String::class.java)
                val nExercise = dataSnapshot.child("complete Exercise").getValue(String::class.java)

                // utilizar email y name según sea necesario
                emailPlayer.text = email
                usernamePlayer.text = name
                numExercise.text = nExercise
                numRoutines.text = nRoutines


            }

            /**
             * funcion por si se cancela algun dato
             * @param error error que reciba la base de datos
             */
            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Error al leer los valores.", error.toException())
            }
        })
    }
}
