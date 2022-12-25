package com.example.myminesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private fun sendIntent(num: Int) {
        val arr = Array(3){num}.toIntArray()
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra("values", arr)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button5).setOnClickListener { sendIntent(5) }
        findViewById<Button>(R.id.button8).setOnClickListener { sendIntent(8) }
        findViewById<Button>(R.id.button10).setOnClickListener { sendIntent(10) }
        findViewById<Button>(R.id.buttonCustom).setOnClickListener {
            startActivity(Intent(this, CustomizeFieldActivity::class.java))
        }
    }
}