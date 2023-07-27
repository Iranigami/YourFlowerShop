package com.example.yourflowershop

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class LeftsActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var typeEditText: EditText
    private lateinit var createButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var leftsview = setContentView(R.layout.activity_lefts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tableLayout = findViewById(R.id.tableLayout)
        typeEditText = findViewById(R.id.type)
        createButton = findViewById(R.id.create)
        createButton.setOnClickListener {
            val type = typeEditText.text.toString()
            val getDataTask = DatabaseTask(this, tableLayout, leftsview, type)
            getDataTask.execute()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ReportActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    class DatabaseTask(private val mContext: Context, private val tableLayout: TableLayout, private val view: Unit, private val type: String) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            var connection: Connection? = null
            var result = ""
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val url = "jdbc:mysql://192.168.1.2/yourflowershop"
                val username = "yourflowershop"
                val password = "120401"
                connection = DriverManager.getConnection(url, username, password)
                // Выполняйте запрос к базе данных здесь
                val db = connection.createStatement()
                var fin = 0.00
                lateinit var resultSet: ResultSet
                if (type == "")
                {resultSet = db.executeQuery("SELECT * FROM products")}
                else
                {resultSet = db.executeQuery("SELECT * FROM `products` WHERE `type`='$type'")}

                while (resultSet.next()) {
                    result += resultSet.getString("product_name")
                    result += "  ,"
                    result += resultSet.getString("type")
                    result += "  ,"
                    var amount = resultSet.getString("amount")
                    result += amount
                    result += "  ,"
                    var price = resultSet.getString("price")
                    result += price
                    result += "  ,"
                    var cost = price.toDouble() * amount.toInt()
                    result += cost.toString()
                    result += "  ;"
                    fin += cost
                }
                result += ",,,Итого: , $fin"
            } catch (ex: Exception) {
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
            view
            tableLayout.removeAllViews()
            var row = TableRow(mContext)
            val params = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            row.layoutParams = params
            val productTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = " Товар  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val dateTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = " Тип  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val amountTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = "  Кол-во  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val priceTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = "  Цена  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val costTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = "  Сто-сть  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            row.addView(productTextView)
            row.addView(dateTextView)
            row.addView(amountTextView)
            row.addView(priceTextView)
            row.addView(costTextView)
            tableLayout.addView(row)
            val rows = result.split(";")
            for (rowString in rows) {
                row = TableRow(mContext)
                val cols = rowString.split(",")
                for (colString in cols) {
                    val cell = TextView(mContext)
                    cell.text = colString
                    cell.setPadding(10, 10, 10, 10)
                    row.addView(cell)
                }
                tableLayout.addView(row)
            }
        }

    }
}