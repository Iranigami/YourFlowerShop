package com.example.yourflowershop

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class EditActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val preProduct = intent.getStringExtra("data")
        val preAmount = intent.getStringExtra("amount")
        val prePrice = intent.getStringExtra("price")
        var edProduct = findViewById<EditText>(R.id.product)
        edProduct.setText(preProduct)
        var edPrice = findViewById<EditText>(R.id.price)
        edPrice.setText(prePrice)
        var edType = findViewById<EditText>(R.id.type)
        val addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener{
            var product = edProduct.text.toString()
            var price = edPrice.text.toString()
            var type = edType.text.toString()
            if (product == "" || price == "")
            {
                Toast.makeText(this, "Заполните поля!", Toast.LENGTH_SHORT).show()
            }
            else {
                val getEditTask = EditTask(product, price, type, this, preAmount)
                getEditTask.execute()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
class EditTask(private val product: String, private val price: String, private val type: String, private val mContext: Context, private val amount: String?) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        var connection: Connection? = null
        var result = ""
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val url = "jdbc:mysql://192.168.1.2/yourflowershop"
            val username = "yourflowershop"
            val password = "120401"
            connection = DriverManager.getConnection(url, username, password)
            val db = connection.createStatement()
            val rs = db.executeQuery("SELECT COUNT(*) FROM products")
            if (rs.next()) {
                var count = rs.getInt(1)+1
                db.executeUpdate("INSERT INTO `products` (`product_id`, `product_name`, `type`, `price`, `amount`) VALUES ('$count', '$product', '$type', '$price','$amount');")
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

}