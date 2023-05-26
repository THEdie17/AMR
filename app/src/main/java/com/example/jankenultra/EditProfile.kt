package com.example.jankenultra

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var editPass: Button
    private lateinit var logOut: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailPlayer: TextView
    private lateinit var usernamePlayer: TextView
    private lateinit var numRoutines: TextView
    private lateinit var numExercise: TextView
    private lateinit var numRacha: TextView
    private lateinit var userPass: String
    private var user: FirebaseUser? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val tf = Typeface.createFromAsset(assets, "fonts/edosz.ttf")
        val tf2 = Typeface.createFromAsset(assets, "fonts/retro.ttf")

        usernamePlayer = findViewById(R.id.nameUser)
        emailPlayer = findViewById(R.id.useEmail)
        numExercise = findViewById(R.id.nExercisis)
        numRoutines = findViewById(R.id.nRutines)
        numRacha = findViewById(R.id.nRacha)

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

        editPass= findViewById(R.id.editPass)
        editPass.setOnClickListener {
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
                val nRacha = dataSnapshot.child("Racha").getValue(String::class.java)
                userPass = dataSnapshot.child("Password").getValue(String::class.java).toString()

                // utilizar email y name según sea necesario
                emailPlayer.text = email
                usernamePlayer.text = name
                numExercise.text = nExercise
                numRoutines.text = nRoutines
                numRacha.text = nRacha


                //Cambiar contraseña

                editPass.setOnClickListener {
                    val builder = AlertDialog.Builder(this@EditProfile)
                    builder.setTitle("Canviar contrasenya")
                    val input = EditText(this@EditProfile)
                    input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    builder.setView(input)

                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        val newPassword = input.text.toString()
                        if (newPassword.length >= 6 && newPassword != userPass && isPasswordValid(newPassword)) {
                            user?.updatePassword(newPassword)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                        this@EditProfile,
                                            "Canvi efectuat",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                        this@EditProfile,
                                            "Canvi no efectuat",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            myRef.child("Password").setValue(newPassword)
                        } else if (newPassword.length < 6) {
                            Toast.makeText(
                                this@EditProfile,
                                "Minim 6 caracters",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (newPassword == userPass) {
                            Toast.makeText(
                                this@EditProfile,
                                "Te que ser una contrasenya diferent",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!isPasswordValid(newPassword)) {
                            Toast.makeText(
                                this@EditProfile,
                                "Te que ser una contrasenya diferent a les anteriors casos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        dialog.dismiss()
                    }

                    builder.setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.cancel()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }

            }

            /**
             * funcion por si se cancela algun dato
             * @param error error que reciba la base de datos
             */
            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Error al leer los valores.", error.toException())
            }

            private fun isPasswordValid(newPassword: String): Boolean {
                val passwordHistory = ArrayList<String>()
                // get the last 5 passwords from the database
                myRef.child("PasswordHistory").limitToLast(5)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (childSnapshot in snapshot.children) {
                                val password = childSnapshot.getValue(String::class.java)
                                password?.let { passwordHistory.add(it) }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.w("Firebase", "Error al leer los valores.", error.toException())
                        }


                    })
                // check if the new password is different from the previous ones
                return !passwordHistory.contains(newPassword)
            }
        })
    }
}
