package com.example.yourflowershop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            val intentRep = Intent(this, LeftsActivity::class.java)
            startActivity(intentRep)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intentRep = Intent(this, MoveActivity::class.java)
            startActivity(intentRep)
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            val intentRep = Intent(this, SellReportActivity::class.java)
            startActivity(intentRep)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }
}
