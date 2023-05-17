package com.example.jankenultra.adapter

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jankenultra.EditProfile
import com.example.jankenultra.Menu
import com.example.jankenultra.R
import com.example.jankenultra.Player
import com.google.firebase.database.FirebaseDatabase
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
    var pathing = ""


    fun render(JugadorModel: Player){
        pathing = JugadorModel.pathing
        nomExercise.text = JugadorModel.nomExercise
        nReplays.text = JugadorModel.nReplays
        nSeries.text = JugadorModel.nSeries
        rest.text = JugadorModel.rest
        imageEjercico1 = JugadorModel.imgId
        try {
            Picasso.get()
                .load(imageEjercico1)
                .into(image)
        } catch (e: ArithmeticException) {
        }
        suggestion.text = JugadorModel.suggestion
        complete_ = JugadorModel.complete
        //changeButton()

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
        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://junkerultra-default-rtdb.europe-west1.firebasedatabase.app/")
        val path = pathing+"/"+nomExercise.text+"/complete"
        if (complete_ == "true"){
            val myRef = database.getReference(path)  // Reemplaza "ruta_del_valor" con la ruta correcta en tu base de datos
            myRef.setValue("true")
        }else{
            val myRef = database.getReference(path)  // Reemplaza "ruta_del_valor" con la ruta correcta en tu base de datos
            myRef.setValue("false")
        }
        changeButton()
    }
}