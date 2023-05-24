package com.example.jankenultra

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion de la pantalla de que la aplicacion esta cargando
 */
class Splash : AppCompatActivity() {
    /**
     * La durada que es veurà aquesta pantalla abans de cambiar a la pantalla principal
     */
    private val duration: Long=6000
    /**
     * La música que suena de fondo
     */
    private lateinit var mp: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mp = MediaPlayer.create(this,R.raw.splashmusic)
        mp.setOnPreparedListener{ mp.start() }
        /**
         * escondemos la barra, pantalla a full
         */
        supportActionBar?.hide()
        //cridem a la funció de canviar activitat
        changeActivity()
    }
    /**
     * Un cop que passi el temps  farà la funció per cambiar de pantalla
     */
    private fun changeActivity(){
        Timer().schedule(duration){
            jumpStart()
        }
    }
    /**
     * La funció ens diu quina pantala mostrarà un cop passat el temps que li hem dit que que es
     * mostri aquesta pantalla un cop passat el temps mostrarà la pantalla principal
     */
    private fun jumpStart() {
        val intent = Intent(this, MainActivity::class.java)
        mp.release()
        startActivity(intent)
    }
}


