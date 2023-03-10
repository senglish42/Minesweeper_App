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
        val valuesList = listOf(height, width, mines)
        val playButton = findViewById<Button>(R.id.button_play)
        val backToMainButton = findViewById<Button>(R.id.button_back)

        height.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                measureOnTextChanged(height)
            }
            override fun afterTextChanged(p0: Editable?) {
                measureAfterTextChanged(height, width, mines)
                setTintToButton(valuesList, playButton)
            }
        })

        width.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                measureOnTextChanged(width)
            }
            override fun afterTextChanged(p0: Editable?) {
                measureAfterTextChanged(width, height, mines)
                setTintToButton(valuesList, playButton)
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
                if (mines.text.toString().toInt() > max) mines.setText(max.toString())
                setTintToButton(valuesList, playButton)
            }
        })

        playButton.setOnClickListener {
            val y = height.text.toString().toIntOrNull()
            val x = width.text.toString().toIntOrNull()
            val m = mines.text.toString().toIntOrNull()
            if (y != null && x != null && m != null)
            {
                val arr = arrayOf(x, y, m).toIntArray()
                val intent = Intent(this, PlayActivity::class.java)
                intent.putExtra("values", arr)
                startActivity(intent)
            } else Toast.makeText(applicationContext, "Fill all the lines to play!", Toast.LENGTH_SHORT).show()
        }

        backToMainButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setTintToButton(valuesList: List<EditText>, button: Button) {
        if (valuesList.count { it.text.toString().isNotEmpty() &&
                    it.text.toString().isNotEmpty()} == 3) {
            button.backgroundTintList = getColorStateList(R.color.deep_yellow)
        } else button.backgroundTintList = getColorStateList(R.color.gray)
    }

    private fun measureOnTextChanged(measure: EditText) {
        if (measure.text.toString().isEmpty()) return
        if (measure.text.toString().toInt() > 50) measure.setText(R.string.max)
    }

    private fun measureAfterTextChanged(measure: EditText, nextMeasure: EditText, mines: EditText) {
        var res = 0
        if (measure.text.toString().isNotEmpty()) {
            res = measure.text.toString().toInt()
            if (nextMeasure.text.toString().isNotEmpty()) res *= nextMeasure.text.toString().toInt()
        } else if (nextMeasure.text.toString().isNotEmpty()) res = nextMeasure.text.toString().toInt()
        mines.hint = "Type a number from 1 to ${if (res > 2) res - 1 else 1}"
        val max = mines.hint.toString().split(' ').last().toInt()
        if (mines.text.isNotEmpty() && mines.text.toString().toInt() > max) {
            mines.setText(max.toString())
        }
    }
}