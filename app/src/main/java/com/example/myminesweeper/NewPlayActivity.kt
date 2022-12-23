package com.example.myminesweeper

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible

class NewPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_play)

        val arr = intent.getIntArrayExtra("values")!!
        val table = findViewById<TableLayout>(R.id.tabMode)
        val minesLeft = findViewById<TextView>(R.id.mines_left)
        val freeButton = findViewById<Button>(R.id.button_free)
        val mineButton = findViewById<Button>(R.id.button_mine)
        val gameOver = findViewById<TextView>(R.id.game_over)
        var height = table.layoutParams.height/ arr[1]
        var width = table.layoutParams.width / arr[0]
        val buttonToMain = findViewById<Button>(R.id.button_back2)
        val buttonRestart = findViewById<Button>(R.id.restart)
        if (arr[0] < arr[1]) {
            table.setPadding(0, 0, 0, 0)
            table.layoutParams.width = height * arr[0]
            width = height
        } else if (arr[1] < arr[0]) {
            table.setPadding(8, 0, 0, 0)
            table.layoutParams.height = width * arr[1]
            height = width
        }
        minesLeft.text = "${minesLeft.text} ${arr[2]}"
        val buttonTriple: MutableList<Triple<ImageButton, Int, Int>>  = mutableListOf()
        for (num in 1..arr[1]) {
            val row = TableRow(this)
            row.layoutParams = TableRow.LayoutParams(Gravity.CENTER, LayoutParams.MATCH_PARENT)
            for (i in 1..arr[0]) {
                val button = ImageButton(this)
                button.isClickable = true
                button.id = View.generateViewId()
                button.layoutParams = TableRow.LayoutParams(width, height)
                button.setBackgroundResource(R.drawable.cell_shape)
                row.addView(button)
                buttonTriple.add(Triple(button, i, num))
            }
            table.addView(row)
        }
        val game = Minesweeper(arr[1], arr[0], arr[2])
        for (i in buttonTriple) {
            val check = findViewById<ImageButton>(i.first.id)
            check.setOnClickListener {
                val cell = game.getCell(i.second - 1, i.third - 1)
                if (cell in listOf('*', '%')) {
                    for (n in buttonTriple) {
                        if (i == n) continue
                        if (n.first.background.constantState == ResourcesCompat.getDrawable(resources, R.drawable.cell_chosen, null)?.constantState) {
                            n.first.setBackgroundResource(R.drawable.cell_shape)
                        }
                        else if (game.getCell(n.second - 1, n.third - 1) in listOf('*', '%')) {
                            n.first.setBackgroundResource(R.drawable.image_flag)
                        }
                    }
                    mineButton.setText(R.string.demine)
//                    if (check.background.constantState != ResourcesCompat.getDrawable(resources, R.drawable.image_flag, null)?.constantState) {
                        check.setBackgroundResource(R.drawable.image_flag_chosen)
//                    }
                } else {
                    mineButton.setText(R.string.mine)
                    for (n in buttonTriple) {
                        if (i == n) continue
                        if (n.first.background.constantState == ResourcesCompat.getDrawable(resources, R.drawable.cell_chosen, null)?.constantState) {
                            n.first.setBackgroundResource(R.drawable.cell_shape)
                        }
                        else if (game.getCell(n.second - 1, n.third - 1) in listOf('*', '%')) {
                            n.first.setBackgroundResource(R.drawable.image_flag)
                        }
                    }
                    check.setBackgroundResource(R.drawable.cell_chosen)
                }
                freeButton.isVisible = true
                mineButton.isVisible = true
                freeButton.setOnClickListener {
                    freeButton.isVisible = false
                    mineButton.isVisible = false
                    game.run(i.second - 1, i.third - 1, "free")
                    val arrField = game.display()
                    for (s in arrField.indices) {
                        when(arrField[s]) {
                            'X'-> {
                                buttonTriple[s].first.setBackgroundResource(if (buttonTriple[s] == i) R.drawable.image_mine_chosen else R.drawable.image_mine)
                                gameOver.isVisible = true
                                buttonToMain.isVisible = true
                                buttonRestart.isVisible = true
                                buttonTriple.map { it.first }.map { it.isClickable = false }
                                buttonRestart.setOnClickListener { finish() ; startActivity(intent) }
                                buttonToMain.setOnClickListener {
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                            }
                            '1' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_one) ; buttonTriple[s].first.isClickable = false }
                            '2' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_two) ; buttonTriple[s].first.isClickable = false }
                            '3' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_three) ; buttonTriple[s].first.isClickable = false }
                            '4' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_four) ; buttonTriple[s].first.isClickable = false }
                            '5' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_five) ; buttonTriple[s].first.isClickable = false }
                            '6' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_six) ; buttonTriple[s].first.isClickable = false }
                            '7' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_seven) ; buttonTriple[s].first.isClickable = false }
                            '8' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.image_eight) ; buttonTriple[s].first.isClickable = false }
                            '0' -> { buttonTriple[s].first.setBackgroundResource(R.drawable.cell) ; buttonTriple[s].first.isClickable = false }
                            '*', '%' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_flag)
                            else -> buttonTriple[s].first.setBackgroundResource(R.drawable.cell_shape)
                        }
                    }
                    if (buttonTriple.map { it.first }.count{ it.isClickable } == arr[2]) {
                        gameOver.text = "WELL DONE! YOU WON A GAME!"
                        gameOver.setTextColor(Color.GREEN)
                        gameOver.isVisible = true
                        buttonToMain.isVisible = true
                        buttonRestart.isVisible = true
                        buttonTriple.map { it.first }.map { it.isClickable = false }
                        buttonRestart.setOnClickListener { finish() ; startActivity(intent) }
                        buttonToMain.setOnClickListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
                mineButton.setOnClickListener {
                    freeButton.isVisible = false
                    mineButton.isVisible = false
                    game.run(i.second - 1, i.third - 1, "mine")
                    if (mineButton.text == "MINE") {
                        i.first.setBackgroundResource(R.drawable.image_flag)
                    } else {
                        i.first.setBackgroundResource(R.drawable.cell_shape)
                    }
                    if (buttonTriple.map { it.first }.count{ it.isClickable } == arr[2]) {
                        gameOver.text = "WELL DONE! YOU WON A GAME!"
                        gameOver.setTextColor(Color.GREEN)
                        gameOver.isVisible = true
                        buttonToMain.isVisible = true
                        buttonRestart.isVisible = true
                        buttonTriple.map { it.first }.map { it.isClickable = false }
                        buttonRestart.setOnClickListener { finish() ; startActivity(intent) }
                        buttonToMain.setOnClickListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}