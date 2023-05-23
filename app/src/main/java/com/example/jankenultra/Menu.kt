package com.example.jankenultra

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


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
    private lateinit var database: FirebaseDatabase
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

        uid = ""
        //val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        database = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
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

                    //Comprobar reboot
                    reboot()
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
        //Botó que porta al perfil del usuari
        profileBtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)//Cambiar el destinio
            startActivity(intent)
        }
        //Ens retorna a la pantalla de rutines
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Obra la pantalla dels exercicis del dilluns
        buttonmonday.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dilluns"
                    //"/DATA_BASE_AMR/hEzKISzJ4fbmg17nhRK122HVXWo1/z_Rutina Dilluns"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Obra la pantalla dels exercicis del dimarts
        tuesdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dimarts"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Obra la pantalla dels exercicis del dimecres
        WednsdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dimecres"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Obra la pantalla dels exercicis del divendres
        fridayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Divendres"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Obra la pantalla dels exercicis del dissabte
        saturdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/zz_Rutina Dissabte"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

    }

    //Cada domingo los ejercicios vuelven al estado "No completado"
    private fun reboot(){
        //Conseguir el numero de la semana
        val calendar = Calendar.getInstance()
        val numeroDiaSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1

        //Crear un archivo donde guardar el dia de la semana
        val sharedPreferences = this.getSharedPreferences("rebootAllProgres.txt", Context.MODE_PRIVATE)

        //Comprobar si la clave existe
        val claveExistente = sharedPreferences.contains("day")
        /*Si la clave existe, y ha pasado 1 semana los datos de ejercicos hechos dentro de cada rutina
         vuelven a valor falso para que la siguiente semana pueda volver a hacerlos*/
        if (claveExistente) {
            database = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
            val valorInt = sharedPreferences.getInt("day", 0)
            if (numeroDiaSemana<valorInt){
                //val myRef = database.getReference("DATA_BASE_AMR/"+uid)

                GlobalScope.launch(Dispatchers.IO) {
                    updateCompleteValuesToFalse("DATA_BASE_AMR/"+uid)
                }
                Log.d("peteFullCharged","El valor: " + valorInt)

            }else{
                Log.d("pp"," Actual: "+numeroDiaSemana+" Anterior:"+valorInt)
            }
        } else {
            Log.d("pete","El valor no existe: " + claveExistente)
        }

        //Serveix per actualitzar/crear
        val editor = sharedPreferences.edit()
        editor.putInt("day", numeroDiaSemana)
        editor.apply()
        val valorInt = sharedPreferences.getInt("day", 0)
        Log.d("pete","El nuevo valor: " + valorInt)
    }

    /*-------------------------------------------------------------------------------------------------------------------------*/
    fun updateCompleteValuesToFalse(myRef :String) {
        //val database = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        val rootCollection = database.getReference(myRef)

        updateCollection(rootCollection)
    }

    fun updateCollection(reference: DatabaseReference) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {
                    for (childSnapshot_1 in childSnapshot.children) {
                        for (childSnapshot_2 in childSnapshot_1.children) {

                            val completeValue = childSnapshot_2.value.toString()
                            if (completeValue == "true") {
                                //childSnapshot_2.ref.child("complete").setValue("false")
                                childSnapshot_2.ref.setValue("false")
                                //Log.d("pp", "-" + completeValue)
                                //Log.d("pp", childSnapshot_2.toString())
                                //Log.d("pp", "Valor cambiado")
                            } else {
                                Log.d("pp", "Valor no cambiado")
                            }


                            // Verificar si hay subreferencias
                            //val subReference = childSnapshot_1.ref.child("subcoleccion")
                            //updateCollection(subReference) // Llamada recursiva para procesar las subreferencias
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("pp", "Error al obtener datos: ${databaseError.message}")
            }
        })
    }
}
