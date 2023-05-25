package com.example.jankenultra

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jankenultra.adapter.JugadorAdapters
import com.google.firebase.database.*

/**
 * Variables utilizadas en la classe
 */
@SuppressLint("StaticFieldLeak")
private lateinit var back: Button
@SuppressLint("StaticFieldLeak")
private lateinit var toLogin: ImageButton
@SuppressLint("StaticFieldLeak")
private lateinit var toProfile: ImageButton
private lateinit var pathing_base: String

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion de los ejercicios
 */
@Suppress("DEPRECATION", "NAME_SHADOWING")
class ScoreList : AppCompatActivity() {
    val jugadors = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        val intent:Bundle? = intent.extras
        pathing_base = intent?.get("PATH").toString()
        consulta()

        toLogin = findViewById(R.id.buttomenu)
        toLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        toProfile = findViewById(R.id.buttonuser)
        toProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        back = findViewById(R.id.button2)
        back.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }

    /**
     * Funcion para crear los recycledViews
     */
    private fun initReciclerView(){
        val reciclerView = findViewById<RecyclerView>(R.id.ReciclerOne)
        reciclerView.layoutManager = LinearLayoutManager(this)
        reciclerView.adapter = JugadorAdapters(jugadors)
    }

    /**
     * Esta funcion la utilizamos para consultar los ejercicios del usuario
     */
    private fun consulta(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        var jumpFirst = false
        val bdreference: DatabaseReference = database.getReference("/$pathing_base")
        bdreference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dr in snapshot.children){
                    if (jumpFirst) {
                        val nomExercise = dr.child("nameExercise").value.toString()
                        val nReplays = dr.child("nReplays").value.toString()
                        val nSeries = dr.child("nSeries").value.toString()
                        val rest = dr.child("rest").value.toString()
                        val imgName = dr.child("imgName").value.toString()
                        val suggestion = dr.child("suggestion").value.toString()
                        val complete = dr.child("complete").value.toString()
                        val j1 = Player(
                            nomExercise,
                            nReplays,
                            nSeries,
                            rest,
                            suggestion,
                            complete,getDrawableResourceId(imgName),pathing_base
                        )
                        if (jugadors.size < 6) {
                            jugadors.add(j1)
                        }else{
                            jugadors.clear()
                        }
                    }else{
                        jumpFirst = true
                    }
                }
                initReciclerView()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error","Base de datos cancelada")
            }
        })
    }

    /**
     * Funcion que consigue el identificador de cada foto para los ejercicios
     * @param imgName Nombre de la imagen de la qual queremos su identificador
     * @return nos devuelve el identificador de la imagen
     */
    @SuppressLint("DiscouragedApi")
    private fun getDrawableResourceId(imgName: String): Int {
        return resources.getIdentifier(imgName, "drawable", packageName)
    }
}