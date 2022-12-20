package com.example.myminesweeper

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CustomizeFieldActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_field)

        val height = findViewById<EditText>(R.id.height_edit)
        val width = findViewById<EditText>(R.id.width_edit)
        val mines = findViewById<EditText>(R.id.mines_edit)

        height.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                height.setSelection(height.length())
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (height.text.toString().isEmpty()) return
                if (height.text.toString().toInt() > 10) {
                    height.setText(height.text.toString().dropLast(1))
                }
            }
            override fun afterTextChanged(p0: Editable?) {
                if (height.text.toString().isNotEmpty()) {
                    var res = height.text.toString().toInt()
                    if (width.text.toString().isNotEmpty()) res *= width.text.toString().toInt()
                    mines.hint = "Type a number from 1 to ${if (res > 0) res else 1}"
                }
            }
        })

        width.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                width.setSelection(width.length())
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (width.text.toString().isEmpty()) return
                if (width.text.toString().toInt() > 10) {
                    width.setText(width.text.toString().dropLast(1))
                }
            }
            override fun afterTextChanged(p0: Editable?) {
                if (width.text.toString().isNotEmpty()) {
                    var res = width.text.toString().toInt()
                    if (height.text.toString().isNotEmpty()) res *= height.text.toString().toInt()
                    mines.hint = "Type a number from 1 to ${if (res > 0) res else 1}"
                }
            }
        })

        mines.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (mines.text.toString().isEmpty()) mines.setText("1")
                mines.setSelection(mines.length())
            }
            override fun afterTextChanged(p0: Editable?) {
                val max = mines.hint.toString().split(' ').last().toInt()
                if (mines.text.toString().toInt() > max) {
                    mines.setText(mines.text.toString().dropLast(1))
                }
            }
        })

        val button = findViewById<Button>(R.id.button_main)
        button.setOnClickListener {
            val y = height.text.toString().toIntOrNull()
            val x = width.text.toString().toIntOrNull()
            val m = mines.text.toString().toIntOrNull()
            if (y != null && x != null && m != null)
            {
                val arr = arrayOf(y, x, m).toIntArray()
                val intent = Intent(this, PlayActivity::class.java)
                intent.putExtra("values", arr)
                startActivity(intent)
            } else Toast.makeText(applicationContext, "Fill all the lines to play!", Toast.LENGTH_SHORT).show()
        }
    }
}