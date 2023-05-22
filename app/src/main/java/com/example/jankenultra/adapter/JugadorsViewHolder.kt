package com.example.jankenultra.adapter

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jankenultra.Player
import com.example.jankenultra.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class JugadorsViewHolder(view: View): RecyclerView.ViewHolder(view){
    val nomExercise= view.findViewById<TextView>(R.id.Nom_exercici1)
    val nReplays = view.findViewById<TextView>(R.id.repetitions1)
    val nSeries = view.findViewById<TextView>(R.id.nSeries)
    val rest = view.findViewById<TextView>(R.id.descans1)
    var imageEjercico1 = 1
    val image  = view.findViewById<ImageView>(R.id.imageEjercico1)
    val suggestion = view.findViewById<TextView>(R.id.consejo_ejercicio1)
    var complete_ = "false"
    var completeExcersise = view.findViewById<Button>(R.id.fet1)

    //Base de datos
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
    var pathing = "" //Path obtenido del usuario actual
    var path = ""    //Path modificadoble para hacer consultas



    fun render(JugadorModel: Player){
        /*Cuando vas hacia abajo recarga la pagina que a la vez manda una solicitud al adaptador
        para que se vuelvan a generar los recylcerview haciendo que se vayan a cumulando y asi haciendo
        que el programa explote*/
        try {
            pathing = JugadorModel.pathing
            nomExercise.text = JugadorModel.nomExercise
            nReplays.text = JugadorModel.nReplays
            nSeries.text = JugadorModel.nSeries
            rest.text = JugadorModel.rest
            imageEjercico1 = JugadorModel.imgId
            Picasso.get()
                .load(imageEjercico1)
                .into(image)
            suggestion.text = JugadorModel.suggestion
            complete_ = JugadorModel.complete
            changeButton()

            /*
            * Cambia el valor pero para que lo haga de True -> False requiere un toque pero al reves requiere dos
            * El boton parpadea de False -> True la primera vez
            * */
            completeExcersise.setOnClickListener {
                if (complete_ == "true"){
                    complete_ = "false"
                }else{
                    complete_ = "true"
                }
                changeValues()
            }
        } catch (e: Exception) {
            print(e)
        }

    }

    fun changeButton(){
        //val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        //val path = pathing+"/"+nomExercise.text+"/complete"
        if (complete_ == "true"){
            //val myRef = database.getReference(path)  // Reemplaza "ruta_del_valor" con la ruta correcta en tu base de datos
            //myRef.setValue("true")
            completeExcersise.text = "Fet"
            completeExcersise.setBackgroundColor(Color.GREEN)
        }else{
            //val myRef = database.getReference(path)  // Reemplaza "ruta_del_valor" con la ruta correcta en tu base de datos
            //myRef.setValue("false")
            completeExcersise.text = "No fet"
            completeExcersise.setBackgroundColor(Color.RED)
        }

    }
    fun changeValues(){
        path = pathing+"/"+nomExercise.text+"/complete"
        if (complete_ == "true"){
            //Cambiar valor de "Completado?"
            var myRef = database.getReference(path)
            myRef.setValue("true")

            //Cambiar valor de "Numero de ejercicios completados"
            val chain_part = path.split("/").toTypedArray()
            path = "/"+chain_part[0]+"/"+chain_part[1]+"/complete Exercise"
            var numeroEntero = 0
            myRef = database.getReference(path)
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nExercise = snapshot.getValue(String::class.java)
                    if (nExercise != null) {
                        numeroEntero = nExercise.toInt() + 1
                        myRef.setValue(numeroEntero.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }else{
            var myRef = database.getReference(path)
            myRef.setValue("false")
            //Cambiar valor de "Numero de ejercicios completados"
            val chain_part = path.split("/").toTypedArray()
            path = "/"+chain_part[0]+"/"+chain_part[1]+"/complete Exercise"
            var numeroEntero = 0
            myRef = database.getReference(path)
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nExercise = snapshot.getValue(String::class.java)
                    if (nExercise != null) {
                        numeroEntero = nExercise.toInt() - 1
                        myRef.setValue(numeroEntero.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        updateRoutineValue()
        changeButton()
    }

    fun updateRoutineValue(){
        var allExTrue = true
        var countNum = 0
        val chain_part = pathing.split("/").toTypedArray()
        //Comprobar si queda algun ejercicio por completar
        val collectionRef = database.getReference("/"+chain_part[0]+"/"+chain_part[1]+"/"+chain_part[2])

        collectionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dr in snapshot.children) {
                    val completeEx = dr.child("complete").value.toString()
                    if (completeEx == "false") {
                        allExTrue = false
                    }
                }
                if (allExTrue) {
                    val myRef = database.getReference("/"+chain_part[0]+"/"+chain_part[1]+"/complete Routines")
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val nRoutines = snapshot.getValue(String::class.java)
                            if (nRoutines != null) {
                                countNum = nRoutines.toInt() + 1
                                myRef.setValue(countNum.toString())
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }else{
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}