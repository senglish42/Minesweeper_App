package com.example.myminesweeper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private fun sendIntent(num: Int) {
        val arr = List(3){num}.toIntArray()
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra("values", arr)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonList = listOf(R.id.button5, R.id.button8, R.id.button10)
        val numList = listOf(5, 8, 10)
        for (i in buttonList.indices) {
            findViewById<Button>(buttonList[i]).setOnClickListener { sendIntent(numList[i]) }
        }
        findViewById<Button>(R.id.buttonCustom).setOnClickListener {
            startActivity(Intent(this, CustomizeFieldActivity::class.java))
        }
    }
}