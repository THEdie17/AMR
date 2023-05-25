package com.example.jankenultra

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace

/**
 * A group of *Diego y Hang*.
 *
 * Classe que maneja la visualizacion de los crèditos
 */
class Credits : AppCompatActivity() {
    /**
     * Los elementos y variables que utilizamos
     */
    lateinit var button: Button
    private val handler = Handler(Looper.getMainLooper())
    private var numeroFragment = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        val tf = Typeface.createFromAsset(assets, "fonts/edosz.ttf")

        /**
         * Llamamamos desde la variable a la id del elemento
         */
        button = findViewById(R.id.buttonreturn)
        button.typeface = tf
        /**
         * Cuando clicamos el botón a que pantalla nos llevara
         */
        button.setOnClickListener {
            val intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Esta funcion intercambia los fragmentos cada cierto tiempo en la pantalla de crèditos
     */
    override fun onResume() {
        super.onResume()
        supportFragmentManager.commit {
            replace<CreditFragment1Logo>(R.id.frame)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
        handler.postDelayed(timeTask, 3000L)
    }

    /**
     * Funcion cuando la aplicacion se ejecuta en 2o plano
     */
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeTask)
    }

    /**
     * Funcion que hace el intercambio de los fragmentos a mostrar
     */
    private val timeTask = object : Runnable {
        override fun run() {
            numeroFragment++
            if (numeroFragment > 2) {
                numeroFragment = 1
            }

            if (numeroFragment == 1) {
                supportFragmentManager.commit {
                    replace<CreditFragment1Logo>(R.id.frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            } else {
                supportFragmentManager.commit {
                    replace<CreditFragment2Author>(R.id.frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            }

            handler.postDelayed(this, 3000L)
        }
    }
}
