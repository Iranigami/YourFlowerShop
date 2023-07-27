package com.example.yourflowershop

import android.os.AsyncTask
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class GetDataTask : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        var connection: Connection? = null
        var result = ""
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://192.168.1.2/yourflowershop"
            val username = "yourflowershop"
            val password = "120401"
            connection = DriverManager.getConnection(url, username, password)
            println("норм")
            // Выполняйте запрос к базе данных здесь
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM users")
            while (resultSet.next()) {
                result += resultSet.getString("username")
                println("да")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            result = "Error: ${ex.message}"
            println("может быть")
        } finally {
            try {
                connection?.close()
            } catch (ex: SQLException) {
                ex.printStackTrace()
                println("нет")
            }
        }
        return result
    }

    override fun onPostExecute(result: String) {
        // Обновите UI с помощью данных, полученных в результате выполнения запроса
    }
}