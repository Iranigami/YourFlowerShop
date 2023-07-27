package com.example.yourflowershop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yourflowershop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var tasksLayout: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var taskEditText: EditText
    private lateinit var addButton: Button
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_sell -> {
                    startActivity(Intent(this, SellActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_arrive -> {
                    startActivity(Intent(this, ArriveActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_writeoff -> {
                    startActivity(Intent(this, WriteoffActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        tasksLayout = findViewById(R.id.tasks_layout)
        titleView = findViewById(R.id.title)
        taskEditText = findViewById(R.id.task_edit_text)
        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val task = taskEditText.text.toString()
            addTask(task)
            taskEditText.text.clear()
        }
    }
    private fun addTask(task: String) {
        val taskView = CardView(this)
        val linearLayout = findViewById<LinearLayout>(R.id.tasks_layout)
        if (task == "")
            Toast.makeText(this, "Пустое задание", Toast.LENGTH_SHORT).show()
        else {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 8, 8, 8)
            taskView.layoutParams = layoutParams
            taskView.cardElevation = 6F  // настройка теней
            taskView.maxCardElevation = 10F
            taskView.useCompatPadding = true
            taskView.radius = 12F
            // Добавить внутренний контент к CardView

            val innerView = layoutInflater.inflate(R.layout.cardview_inner_layout, null)
            val taskTextView = innerView.findViewById<TextView>(R.id.taskText)
            taskTextView.text = task
            taskView.addView(innerView)

            // Добавить CardView в LinearLayout

            linearLayout.addView(taskView)
        }
        taskView.setOnClickListener {
            tasksLayout.removeView(taskView)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}