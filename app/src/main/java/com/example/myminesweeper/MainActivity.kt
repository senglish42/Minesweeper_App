package com.example.myminesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button5).setOnClickListener {
            val arr = arrayOf(5, 5, 5).toIntArray()
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("values", arr)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button8).setOnClickListener {
            val arr = arrayOf(8, 8, 8).toIntArray()
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("values", arr)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button10).setOnClickListener {
            val arr = arrayOf(10, 10, 10).toIntArray()
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("values", arr)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCustom).setOnClickListener {
            startActivity(Intent(this, CustomizeFieldActivity::class.java))
        }
    }
}