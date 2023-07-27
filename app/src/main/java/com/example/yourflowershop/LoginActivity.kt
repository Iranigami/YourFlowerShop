package com.example.yourflowershop

import android.R.attr.password
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitylogin)
        val sharedPref = this.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isUserLoggedIn", false)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        if (isLoggedIn) {
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        } else {


        loginButton.setOnClickListener {
            val user = usernameEditText.text.toString()
            val pass = passwordEditText.text.toString()

            // проверка введенных данных
            //if (user == "username" && pass == "password")
            if (authenticate(user, pass))
            {
                with (sharedPref.edit()) {
                    putBoolean("isUserLoggedIn", true)
                    apply()
                }
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            } else {
                // выводим сообщение об ошибке
                if (user == "" || pass == "")
                    Toast.makeText(this, "Заполните поля!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Ошибка авторизации!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    }
    fun authenticate(user: String, pass: String): Boolean {
        return (user == "username" && pass == "password")||
                (user=="123" && pass == "321") ||
                (user=="iranigami" && pass == "123")
    }


}