package com.example.jankenultra

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion de las rutinas
 */
@Suppress("PrivatePropertyName")
class Menu : AppCompatActivity() {
    // Creamos unas variables relacionadas a los elementos que se muestran en la capa vista
    private lateinit var auth: FirebaseAuth
    private lateinit var profileBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var buttonmonday: ImageButton
    private lateinit var tuesdayButton: ImageButton
    private lateinit var wednesdayButton: ImageButton
    private lateinit var fridayButton: ImageButton
    private lateinit var saturdayButton: ImageButton
    private lateinit var pathing_user: String
    private lateinit var uid: String
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
        wednesdayButton = findViewById(R.id.WednsdayButton)
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
                val uidUser = dataSnapshot.child("Uid").getValue(String::class.java)
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


        //Botón que lleva al perfil del usuario
        profileBtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)//Cambiar el destinio
            startActivity(intent)
        }
        //Nos devuelve al Menú principal
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Abre la pantalla de los ejercicios del Lunes
        buttonmonday.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dilluns"
                    //"/DATA_BASE_AMR/hEzKISzJ4fbmg17nhRK122HVXWo1/z_Rutina Dilluns"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Abre la pantalla de los ejercicios del Martes
        tuesdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dimarts"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Abre la pantalla de los ejercicios del Miércoles
        wednesdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Dimecres"
            val intent = Intent(this, ScoreList::class.java)//Cambiar el destinio
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Abre la pantalla de los ejercicios del Viernes
        fridayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/z_Rutina Divendres"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

        //Abre la pantalla de los ejercicios del Sábado
        saturdayButton.setOnClickListener {
            pathing_user = "DATA_BASE_AMR/"+uid+"/zz_Rutina Dissabte"
            val intent = Intent(this, ScoreList::class.java)
            intent.putExtra("PATH",pathing_user)
            startActivity(intent)
        }

    }

    //Cada domingo los ejercicios vuelven al estado "No completado"
    /**
     * Esta funcion devuelve los ejercicios que se han COMPLETADO a NO COMPLETADO cada domingo
     */
    @OptIn(DelicateCoroutinesApi::class)
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
            var myRef = database.getReference("DATA_BASE_AMR/$uid/Racha")
            val valorInt = sharedPreferences.getInt("day", 0)

            if (numeroDiaSemana<valorInt){
                //val myRef = database.getReference("DATA_BASE_AMR/"+uid)
                GlobalScope.launch(Dispatchers.IO) {
                    updateCompleteValuesToFalse("DATA_BASE_AMR/$uid")
                }
                //Log.d("peteFullCharged","El valor: " + valorInt)

            //Empieza racha
            }else if(numeroDiaSemana==(valorInt+1) && (numeroDiaSemana==1 || numeroDiaSemana==2 || numeroDiaSemana==3 || numeroDiaSemana==6)){
                moreStreak()
                Log.d("pp","Racha1 Actual: "+numeroDiaSemana+" Anterior:"+valorInt)
            }else if(numeroDiaSemana==(valorInt+2) && numeroDiaSemana==5){
                moreStreak()
                Log.d("pp","Racha1 Actual: "+numeroDiaSemana+" Anterior:"+valorInt)
            }else{
                myRef.setValue("0")

            }/*else{
                Log.d("pp"," Actual: "+numeroDiaSemana+" Anterior:"+valorInt)
            }*/
        }/* else {
            Log.d("pete","El valor no existe: " + claveExistente)
        }*/

        //Serveix per actualitzar/crear
        val editor = sharedPreferences.edit()
        editor.putInt("day", numeroDiaSemana)
        editor.apply()
    }

    /*-------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Da inicio a la actualización de los datos
     */
    private fun updateCompleteValuesToFalse(myRef :String) {
        //val database = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        val rootCollection = database.getReference(myRef)
        updateCollection(rootCollection)
    }

    /**
     * Funcion para que actualize de forma recursiva todos los ejercios
     * @param reference referencia donde estan alojado los objetos en Firebase
     */
    private fun updateCollection(reference: DatabaseReference) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (childSnapshot in dataSnapshot.children) {
                    for (childSnapshot_1 in childSnapshot.children) {
                        for (childSnapshot_2 in childSnapshot_1.children) {

                            val completeValue = childSnapshot_2.value.toString()
                            if (completeValue == "true") {
                                childSnapshot_2.ref.setValue("false")
                            } else {
                                Log.d("pp", "Valor no cambiado")
                            }
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("pp", "Error al obtener datos: ${databaseError.message}")
            }
        })
    }

    private fun moreStreak(){
        var Streak = 0
        var myRef = database.getReference("DATA_BASE_AMR/$uid/Racha")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nRacha = snapshot.getValue(String::class.java)
                if (nRacha != null) {
                    Streak = nRacha.toInt() + 1
                    myRef.setValue(Streak.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
