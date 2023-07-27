package com.example.yourflowershop

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.security.AccessController.getContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

class WriteoffBottomFragment():BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        var view = inflater.inflate(R.layout.writeoff_fragment_bottom_sheet, container, false)
        val edProduct = view.findViewById<EditText>(R.id.edProduct)
        val edPrice = view.findViewById<EditText>(R.id.edPrice)
        val edAmount = view.findViewById<EditText>(R.id.edAmount)
        val reason = view.findViewById<EditText>(R.id.reason)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener{
            if (edProduct.text.trim().toString().isNotEmpty() && edPrice.text.trim().toString().isNotEmpty() && edAmount.text.trim().toString().isNotEmpty()){
                var product = edProduct.text.trim().toString()
                var price = edPrice.text.trim().toString()
                var amount = edAmount.text.trim().toString()
                var con = requireContext()
                var reas = reason.text.trim().toString()
                val getDataTask = WriteoffTask(product, price, amount, con, reas)
                getDataTask.execute()
                //startActivity(Intent(requireContext(), SellActivity::class.java).putExtra("data",product))
                dismiss()
            }
            else{
                Toast.makeText(requireContext(), "Заполните поля!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}
class WriteoffTask(private val product: String, private val price: String, private val amount: String, private val mContext: Context, private val reason: String,) : AsyncTask<Void, Void, String>() {

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
            val dbProduct = db.executeQuery("SELECT * FROM products WHERE product_name = '$product'")
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = dateFormat.format(currentDate)
            var am = "-$amount"
            if (!(dbProduct.next()))
            {
                Toast.makeText(mContext, "Не хватает товара на складе", Toast.LENGTH_SHORT).show()
            }
            else {
                val rs = db.executeQuery("SELECT COUNT(*) FROM writeoffs")
                if (rs.next()) {
                    var count = rs.getInt(1) + 1
                    db.executeUpdate("INSERT INTO `writeoffs` (`writeoff_id`, `price`, `product`, `user`, `date`, `amount`, `reason`) VALUES ('$count', '$price', '$product', 'Дария', '$formattedDate','$amount', '$reason');")
                    db.executeUpdate("UPDATE `products` SET `amount` = `amount` - $amount WHERE `product_name`= '$product';")
                }
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