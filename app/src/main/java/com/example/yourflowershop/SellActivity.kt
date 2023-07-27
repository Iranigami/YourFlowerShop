package com.example.yourflowershop

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class SellActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sellview = setContentView(R.layout.activity_sell)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val linearLayout = findViewById<LinearLayout>(R.id.prod_layout)
        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            var bottomFragment = SellBottomFragment();
            bottomFragment.show(supportFragmentManager, "TAG")
        }

        val getDataTask = DatabaseTask(this, linearLayout, sellview)
        getDataTask.execute()

    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    class DatabaseTask(private val mContext: Context, private val lLayout: LinearLayout, private val view: Unit) : AsyncTask<Void, Void, String>() {

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
                val db = connection.createStatement()
                lateinit var productName: String
                val resultSet = db.executeQuery("SELECT * FROM sells")
                while (resultSet.next()) {
                    result += resultSet.getString("date")
                    result += ","
                    result += resultSet.getString("product")
                    result += ","
                    val amount = resultSet.getString("amount")
                    result += amount
                    result += ","
                    val price = resultSet.getString("price")
                    result += price
                    result += ","
                    val cost = price.toDouble() * amount.toInt()
                    result += cost.toString()
                    result += ","
                    result += resultSet.getString("type")
                    result += ";"

                }
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
            lLayout
            view
            val rows = result.split(";")
            for (rowString in rows) {
                val prodView = CardView(mContext)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(1, 0, 0, 0)
                prodView.layoutParams = layoutParams
                prodView.cardElevation = 6F  // настройка теней
                prodView.maxCardElevation = 10F
                prodView.useCompatPadding = true
                prodView.radius = 12F
                val innerView = View.inflate(mContext, R.layout.productcardview_layout, null) as CardView
                val productName = innerView.findViewById<TextView>(R.id.product_name)
                val aAndP = innerView.findViewById<TextView>(R.id.amount_and_price)
                val cost = innerView.findViewById<TextView>(R.id.cost)
                val icon = innerView.findViewById<ImageView>(R.id.icon)

                val cols = rowString.split(",")
                var i = 0
                var price = ""
                for (colString in cols) {
                    when (i) {
                        1 -> {productName.text = colString}
                        2 -> {price = colString + " x "}
                        3 -> {aAndP.text = price + colString}
                        4 -> {cost.text = colString}
                       /*5 -> {
                            when (colString) {
                                "Срез" ->{icon.setImageResource(R.drawable.flower)}
                                "Горшечка"->{icon.setImageResource(R.drawable.flowerpot)}
                                "Горшки"->{icon.setImageResource(R.drawable.pot)}
                                "Рассада"->{icon.setImageResource(R.drawable.sprout)}
                                "Упаковка"->{icon.setImageResource(R.drawable.tie)}
                                "Химия/Удобрения/Грунт"->{icon.setImageResource(R.drawable.pack)}
                                "Декор"->{icon.setImageResource(R.drawable.decor)}
                            }

                        } */
                    }
                    i++
                }
                prodView.addView(innerView)
                lLayout.addView(prodView, 0)
            }
            lLayout.removeViewAt(0)
        }
    }
}