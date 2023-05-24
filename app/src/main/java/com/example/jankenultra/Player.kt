package com.example.jankenultra

/**
 * A group of *Diego y Hang*.
 *
 * És una classe intermedia utilizada para crear objetos que seran utilizados por otra classe
 * para mostrar los ejercicios y sus datos
 *
 * @param nomExercise nombre del ejercicio
 * @param nReplays numero de repeticiones por serie
 * @param nSeries numero de series que se deben hacer
 * @param rest tiempo de descanso entre series
 * @param suggestion consejo o descripción de como hacer el ejercicio
 * @param complete para saber si han hecho el ejercicio o no
 * @param imgId imagen de ayuda para saber como hacer el ejercicio
 * @param pathing recorrido donde se encuentran los ejercicios en el Firebase.
 */
class Player (val nomExercise:String, val nReplays:String, val nSeries:String, val rest:String, val suggestion:String, val complete: String, val imgId: Int, val pathing:String)