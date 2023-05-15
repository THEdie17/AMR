package com.example.jankenultra.adapter

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jankenultra.Menu
import com.example.jankenultra.R
import com.example.jankenultra.Player
import com.squareup.picasso.Picasso
class JugadorsViewHolder(view: View): RecyclerView.ViewHolder(view){
    val nomExercise= view.findViewById<TextView>(R.id.Nom_exercici1)
    val nReplays = view.findViewById<TextView>(R.id.repetitions1)
    val nSeries = view.findViewById<TextView>(R.id.nSeries)
    val rest = view.findViewById<TextView>(R.id.descans1)
    var imageEjercico1 = ""
    val image  = view.findViewById<ImageView>(R.id.imageEjercico1)
    val suggestion = view.findViewById<TextView>(R.id.consejo_ejercicio1)
    var complete = ""
    var completeExcersise = view.findViewById<Button>(R.id.fet1)


    fun render(JugadorModel: Player){
        nomExercise.text = JugadorModel.nomExercise
        nReplays.text = JugadorModel.nReplays
        nSeries.text = JugadorModel.nSeries
        rest.text = JugadorModel.rest
        imageEjercico1 = JugadorModel.imgName
        Picasso.get()
            .load(imageEjercico1)
            .into(image)
        suggestion.text = JugadorModel.suggestion

    }
}