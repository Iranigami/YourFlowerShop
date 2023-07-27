package com.example.yourflowershop

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.OutputStreamWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val exitButton = findViewById<Button>(R.id.exit)
        exitButton.setOnClickListener {
            val sharedPref = this.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putBoolean("isUserLoggedIn", false)
                apply()
            }
            val intentMain = Intent(this, LoginActivity::class.java)
            startActivity(intentMain)
        }
        val reportButton = findViewById<TextView>(R.id.set1)
        reportButton.setOnClickListener{
            //создать отчет
            val intentRep = Intent(this, ReportActivity::class.java)
            startActivity(intentRep)
        }
        val importButton = findViewById<TextView>(R.id.set2)
        importButton.setOnClickListener{
            //импорт
            Toast.makeText(this, "База данных успешно обновлена!", Toast.LENGTH_SHORT).show()
        }
        val exportButton = findViewById<TextView>(R.id.set3)
        exportButton.setOnClickListener{
            //экспорт
            Toast.makeText(this, "База данных успешно экспортирована!", Toast.LENGTH_SHORT).show()
        }
        val cleanButton = findViewById<TextView>(R.id.set4)
        cleanButton.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Вопрос")
            builder.setMessage("Вы уверены, что хотите очистить базу данных? Это действие нельзя отменить!")

            builder.setPositiveButton("Да") { dialog, which ->
                val cleanDB = SettingsDBTask(this, 4)
                //cleanDB.execute()
                Toast.makeText(this, "База данных успешно очищена!", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Отменить") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }


    private fun writeToFile(data: String, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("database.dt", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }
}



class SettingsDBTask(private val mContext: Context, private val task: Int) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        var connection: Connection? = null
        var result = "База данных успешно очищена"
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://192.168.1.2/yourflowershop"
            val username = "yourflowershop"
            val password = "120401"
            connection = DriverManager.getConnection(url, username, password)
            val db = connection.createStatement()
            when (task) {
                1 ->{}
                2 ->{

                }
                3 ->{}
                4-> {
                db.executeUpdate("DELETE FROM `arrives`;")
                db.executeUpdate("DELETE FROM `sells`;")
                db.executeUpdate("DELETE FROM `writeoffs`;")
                db.executeUpdate("DELETE FROM `products`;")
            }
                else -> {}
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            result = "Нет подключения к базе данных"
        } finally {
            try {
                connection?.close()
            } catch (ex: SQLException) {
                ex.printStackTrace()
                result = "Произошла ошибка"
            }
        }
        return result
    }

        override fun onPostExecute(result: String) {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("")
            builder.setMessage(result)

            builder.setPositiveButton("ОК") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }