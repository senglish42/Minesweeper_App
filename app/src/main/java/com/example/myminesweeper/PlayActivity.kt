package com.example.myminesweeper

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.content.res.ColorStateList
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

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val arr = intent.getIntArrayExtra("values")!!
        val table = findViewById<TableLayout>(R.id.tabMode)
        val textViewList: List<TextView> = listOf(findViewById(R.id.mines_left), findViewById(R.id.game_over))
        val freeMineButtonList: List<Button> = listOf(findViewById(R.id.button_free), findViewById(R.id.button_mine))
        var height = table.layoutParams.height/ arr[1]
        var width = table.layoutParams.width / arr[0]
        val buttonsFinishList = listOf(findViewById<Button>(R.id.button_back2), findViewById(R.id.restart))
        val buttonTriple: MutableList<Triple<ImageButton, Int, Int>>  = mutableListOf()
        if (arr[0] < arr[1]) {
            table.layoutParams.width = height * arr[0]
            width = height
        } else if (arr[1] < arr[0]) {
            table.layoutParams.height = width * arr[1]
            height = width
        }
        textViewList[0].text = "${textViewList[0].text} ${arr[2]}"
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
                    freeMineButtonList[0].isEnabled = false
                    freeMineButtonList[0].setBackgroundColor(Color.GRAY)
                    setMineTextAndDrawable(freeMineButtonList, check, R.string.demine, R.drawable.image_flag_chosen)
                } else {
                    freeMineButtonList[0].isEnabled = true
                    freeMineButtonList[0].setBackgroundColor(Color.parseColor("#F0B040"))
                    setMineTextAndDrawable(freeMineButtonList, check, R.string.mine, R.drawable.cell_chosen)
                }
                checkIfNoActionAfterClick(buttonTriple, i, game)
                freeMineButtonList.forEach { it.isVisible = true }
                freeMineButtonList[0].setOnClickListener {
                    freeMineOnClick(freeMineButtonList, game, i, "free")
                    setBackgroundOnFields(game, buttonTriple, buttonsFinishList, textViewList, i)
                    val isClickableOnly = buttonTriple.map { it.first }.count{ it.isClickable } == arr[2]
                    val isAllOpened = game.getArr().count { it == '*' } == arr[2]
                    if (isClickableOnly || isAllOpened) {
                        if (!isAllOpened) {
                            for (n in buttonTriple) {
                                if (n.first.background.constantState == ResourcesCompat.getDrawable(resources, R.drawable.cell_shape, null)?.constantState) {
                                    n.first.setBackgroundResource(R.drawable.image_flag)
                                }
                            }
                        }
                        winningGame(textViewList, buttonsFinishList, buttonTriple)
                    }
                }
                freeMineButtonList[1].setOnClickListener {
                    freeMineOnClick(freeMineButtonList, game, i, "mine")
                    setCellShapeOnMine(i, freeMineButtonList[1].text.toString(), textViewList, arr[2] - game.getArr().count { it in listOf('*', '%') })
                    val isClickableOnly = buttonTriple.map { it.first }.count{ it.isClickable } == arr[2]
                    val isAllOpened = game.getArr().count { it == '*' } == arr[2]
                    if (isClickableOnly || isAllOpened) {
                        winningGame(textViewList, buttonsFinishList, buttonTriple)
                    }
                }
            }
        }
    }

    private fun setBackgroundOnFields(game: Minesweeper, buttonTriple: MutableList<Triple<ImageButton, Int, Int>>, buttonsFinishList: List<Button>, textViewList: List<TextView>, i: Triple<ImageButton, Int, Int>){
        val arrField = game.display()
        for (s in arrField.indices) {
            when(arrField[s]) {
                'X'-> {
                    buttonTriple[s].first.setBackgroundResource(if (buttonTriple[s] == i) R.drawable.image_mine_chosen else R.drawable.image_mine)
                    gameIsFinished(textViewList, buttonsFinishList, buttonTriple)
                }
                '1' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_one)
                '2' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_two)
                '3' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_three)
                '4' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_four)
                '5' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_five)
                '6' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_six)
                '7' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_seven)
                '8' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_eight)
                '0' -> buttonTriple[s].first.setBackgroundResource(R.drawable.cell)
                '*', '%' -> buttonTriple[s].first.setBackgroundResource(R.drawable.image_flag)
                else -> buttonTriple[s].first.setBackgroundResource(R.drawable.cell_shape)
            }
            if (arrField[s] in '0'..'9') buttonTriple[s].first.isClickable = false
        }
    }

    private fun gameIsFinished(textViewList: List<TextView>, buttonsFinishList: List<Button>, buttonTriple: MutableList<Triple<ImageButton, Int, Int>>) {
        textViewList[1].isVisible = true
        buttonsFinishList.forEach { it.isVisible = true }
        buttonTriple.map { it.first }.map { it.isClickable = false }
        buttonsFinishList[0].setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        buttonsFinishList[1].setOnClickListener { finish() ; startActivity(intent) }
    }

    private fun winningGame(textViewList: List<TextView>, buttonsFinishList: List<Button>, buttonTriple: MutableList<Triple<ImageButton, Int, Int>>) {
        textViewList[0].text = "MINES LEFT: 0"
        textViewList[1].setText(R.string.winner)
        textViewList[1].setTextColor(Color.GREEN)
        gameIsFinished(textViewList, buttonsFinishList, buttonTriple)
    }

    private fun checkIfNoActionAfterClick(buttonTriple: MutableList<Triple<ImageButton, Int, Int>>, i: Triple<ImageButton, Int, Int>, game: Minesweeper) {
        for (n in buttonTriple) {
            if (i == n) continue
            if (n.first.background.constantState == ResourcesCompat.getDrawable(resources, R.drawable.cell_chosen, null)?.constantState) {
                n.first.setBackgroundResource(R.drawable.cell_shape)
            }
            else if (game.getCell(n.second - 1, n.third - 1) in listOf('*', '%')) {
                n.first.setBackgroundResource(R.drawable.image_flag)
            }
        }
    }

    private fun setMineTextAndDrawable(freeMineButtonList: List<Button>, check: ImageButton, textId: Int, drawableId: Int) {
        freeMineButtonList[1].setText(textId)
        check.setBackgroundResource(drawableId)
    }

    private fun freeMineOnClick(freeMineButtonList: List<Button>, game: Minesweeper, i: Triple<ImageButton, Int, Int>, str: String) {
        freeMineButtonList.forEach { it.isVisible = false }
        game.run(i.second - 1, i.third - 1, str)
    }

    private fun setCellShapeOnMine(i: Triple<ImageButton, Int, Int>, str: String, textViewList: List<TextView>, num: Int) {
        if (str == "MINE") i.first.setBackgroundResource(R.drawable.image_flag)
        else i.first.setBackgroundResource(R.drawable.cell_shape)
        textViewList[0].text = "MINES LEFT: $num"
    }

    private fun check(u: () -> Unit = ::TODO) {

    }
}