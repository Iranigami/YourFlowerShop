package com.example.yourflowershop

import android.app.DatePickerDialog
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
import java.util.Calendar

class SellReportActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var typeEditText: EditText
    private lateinit var createButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var leftsview = setContentView(R.layout.activity_move)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tableLayout = findViewById(R.id.tableLayout)
        typeEditText = findViewById(R.id.type)
        createButton = findViewById(R.id.create)
        val fromDate = findViewById<EditText>(R.id.fromDate)
        val toDate = findViewById<EditText>(R.id.toDate)
        fromDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Установка выбранной даты в EditText
                val selectedDate = "${year}-${month + 1}-${dayOfMonth}"
                fromDate.setText(selectedDate)
            }

            val datePickerDialog = DatePickerDialog(this, dateListener, year, month, day)
            datePickerDialog.show()
        }
        toDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Установка выбранной даты в EditText
                val selectedDate = "${year}-${month + 1}-${dayOfMonth}"
                toDate.setText(selectedDate)
            }

            val datePickerDialog = DatePickerDialog(this, dateListener, year, month, day)
            datePickerDialog.show()
        }
        createButton.setOnClickListener {
            val type = typeEditText.text.toString()
            val getDataTask = DatabaseTask(this, tableLayout, leftsview, type, fromDate.text.toString(), toDate.text.toString())
            getDataTask.execute()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ReportActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    class DatabaseTask(private val mContext: Context, private val tableLayout: TableLayout, private val view: Unit,  private var type: String, private var from: String, private var to: String) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String {
            var connection: Connection? = null
            var result = ""
            if (type == "")
                type = "products.type"
            else
                type = "'$type'"
            if (from == "")
                from = "'2020-01-01'"
            else
                from = "'$from'"
            if (to == "")
                to = "'2099-12-31'"
            else
                to = "'$to'"
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val url = "jdbc:mysql://192.168.1.2/yourflowershop"
                val username = "yourflowershop"
                val password = "120401"
                connection = DriverManager.getConnection(url, username, password)
                // Выполняйте запрос к базе данных здесь
                val db = connection.createStatement()
                var fin = 0.00
                var resultSet = db.executeQuery(
                    "SELECT products.product_name, SUM(sells.amount) AS total_amount, products.price, SUM(sells.amount * products.price) AS total_price\n" +
                            "FROM products\n" +
                            "JOIN sells ON products.product_name = sells.product\n" +
                            "WHERE sells.date BETWEEN $from AND $to AND products.type = $type\n" +
                            "GROUP BY products.product_name, products.price;\n")
                while (resultSet.next()) {
                    result += resultSet.getString("product_name")
                    result += "  ,"
                    result += resultSet.getString("total_amount")
                    result += "  ,"
                    result += resultSet.getString("price")
                    result += "  ,"
                    val cost = resultSet.getString("total_price")
                    result += cost
                    result += "  ;"
                    fin += cost.toDouble()
                }
                result += ",,Итого: , $fin"
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
                text = " Всего продано  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val amountTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = "  Цена  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            val priceTextView = TextView(mContext).apply {
                id = View.generateViewId()
                text = "  Сумма  "
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setBackgroundColor(ContextCompat.getColor(context, R.color.text_color))
            }
            row.addView(productTextView)
            row.addView(dateTextView)
            row.addView(amountTextView)
            row.addView(priceTextView)
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