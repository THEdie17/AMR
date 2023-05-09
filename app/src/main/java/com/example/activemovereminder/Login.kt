package com.example.activemovereminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.ResultSet
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
/*
class Login  : AppCompatActivity() {
    val url = "jdbc:postgresql://horton.db.elephantsql.com:5432/deeschbb"
    val api_databse = "c63faa11-bea0-4e26-8ec0-caef3c504ded"
    val username = "deeschbb"
    val password = "vgf1S651m1Jr0ZibBBgOQA-x7z8F6TR4"

    val databaseClient = DatabaseClient(url, username, password)
*/

class Login  : AppCompatActivity() {
    val url = "jdbc:postgresql://horton.db.elephantsql.com:5432/deeschbb"
    val api_databse = "c63faa11-bea0-4e26-8ec0-caef3c504ded"
    val username = "deeschbb"
    val password = "vgf1S651m1Jr0ZibBBgOQA-x7z8F6TR4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Class.forName("org.postgresql.Driver")
                val connection = DriverManager.getConnection(url, username, password)

                val statement = connection.createStatement()
                val query = "SELECT * FROM users"
                val resultSet = statement.executeQuery(query)

                while (resultSet.next()) {
                    val column1 = resultSet.getString("id_user")
                    val column2 = resultSet.getString("name")
                    // ... procesa los datos según tus necesidades
                    println("id_user: $column1, name: $column2")
                }

                resultSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Espera un tiempo para que se completen las operaciones de la base de datos
        Thread.sleep(5000)
    }
}
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Conectarse a la base de datos en un hilo de fondo
        GlobalScope.launch(Dispatchers.IO) {
            databaseClient.connect()

            // Ejemplo de consulta
            val query = "SELECT * FROM users"
            val result: ResultSet? = databaseClient.executeQuery(query)

            withContext(Dispatchers.Main) {
                // Procesa el resultado de la consulta
                // Actualiza la interfaz de usuario según sea necesario
            }
        }
    }
}
*/