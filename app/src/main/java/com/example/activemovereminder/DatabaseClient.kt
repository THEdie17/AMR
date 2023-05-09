package com.example.activemovereminder

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class DatabaseClient(private val url: String, private val username: String, private val password: String) {
    private var connection: Connection? = null

    fun connect() {
        Class.forName("org.postgresql.Driver")
        connection = DriverManager.getConnection(url, username, password)
    }

    fun disconnect() {
        connection?.close()
    }

    fun executeQuery(query: String): ResultSet? {
        val statement: Statement? = connection?.createStatement()
        return statement?.executeQuery(query)
    }
    // Agrega otras funciones para ejecutar diferentes operaciones con la base de datos
}