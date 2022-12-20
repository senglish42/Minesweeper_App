package com.example.myminesweeper

import android.annotation.SuppressLint
import android.content.Intent
import kotlin.random.Random
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

fun Array<Char>.fillRandom(num: Int, capacity: Int) {
    val rand = Random.Default
    repeat(num) {
        while (true) {
            val res = rand.nextInt(0, capacity)
            if (this[res] == '+') { this[res] = 'X' ; break }
        }
    }
}

class PlayActivity : AppCompatActivity() {

    private fun setToast(str: String) = Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()

    private fun isCorrectInput(arrSet: List<String>, arr: Array<Char>, width: Int): Boolean {
        val x = arrSet[0].toInt()
        val y = arrSet[1].toInt()
        val validStr = arrayOf("mine", "free")
        return if (arrSet.size != 3 || x !in 1..10 || y !in 1..10
            || arrSet[2] !in validStr) { setToast("Parameters are not valid, type again") ; false }
        else when (arr[(y - 1) * width + x - 1]) {
            in '1'..'8' -> { setToast("There is a number in this cell!") ; false }
            '0' -> { setToast("The cell is already free!"); false }
            else -> true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val setterText = findViewById<TextView>(R.id.setter_text)
        val ground = findViewById<TextView>(R.id.game)
        val arr = intent.getIntArrayExtra("values")
        if (arr == null) ground.text = "Error while sending intent"
        else {
            val game = Minesweeper(arr[0], arr[1], arr[2])
            ground.text = game.display()
            val check = findViewById<TextView>(R.id.button_play)
            check.setOnClickListener {
                val setter = findViewById<TextView>(R.id.setter_edit)
                val arrSet = setter.text.toString().split(' ').map { it.lowercase(Locale.ROOT) }
                setter.text = ""
                if (isCorrectInput(arrSet, game.getArr(), game.getWidth())) {
                    game.run(arrSet[0].toInt() - 1, arrSet[1].toInt() - 1, arrSet[2])
                    ground.text = game.display()
                    if (!game.isContinued()) {
                        if (game.isPlayerLost()) {
                            setterText.setTextColor(ContextCompat.getColor(this, R.color.red))
                            setterText.text = "You stepped on a mine and failed!"
                        } else {
                            setterText.setTextColor(ContextCompat.getColor(this, R.color.teal_700))
                            setterText.text = "Congratulations! You found all the mines!"
                        }
                        check.setText("BACK TO MAIN")
                        check.setOnClickListener {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    class Minesweeper(private var height: Int, private val width: Int = 0, private val mines: Int = 0) {
        private var capacity = width * height
        private val arr = Array(capacity){'+'}
        private var minesLeft = mines
        private var isMineChosen = false

        init { arr.fillRandom(this.mines, this.capacity) }

        fun getArr() = this.arr
        fun getWidth() = this.width
        fun isPlayerLost() = this.isMineChosen

        private fun unlockCells(x: Int, y: Int, mine: Array<Char> = arrayOf('X', '*'), free: Array<Char> = arrayOf('+', '%')) {
            var count = 0
            if (x > 0 && arr[y * width + x - 1] in mine) ++count
            if (x < width - 1 && arr[y * width + x + 1] in mine) ++count
            if (y > 0 && arr[(y - 1) * width + x] in mine) ++count
            if (y < height - 1 && arr[(y + 1) * width + x] in mine) ++count
            if (y > 0 && x > 0 && arr[(y - 1) * width + x - 1] in mine) ++count
            if (y > 0 && x < width - 1 && arr[(y - 1) * width + x + 1] in mine) ++count
            if (y < height - 1 && x > 0 && arr[(y + 1) * width + x - 1] in mine) ++count
            if (y < height - 1 && x < width - 1 && arr[(y + 1) * width + x + 1] in mine) ++count
            if (count > 0) arr[y * width + x] = Character.forDigit(count, 10)
            else {
                arr[y * width + x] = '0'
                if (x > 0 && arr[y * width + x - 1] in free) unlockCells(x - 1, y)
                if (x < width - 1 && arr[y * width + x + 1] in free) unlockCells(x + 1, y)
                if (y > 0 && arr[(y - 1) * width + x] in free) unlockCells(x, y - 1)
                if (y < height - 1 && arr[(y + 1) * width + x] in free) unlockCells(x, y + 1)
                if (y > 0 && x > 0 && arr[(y - 1) * width + x - 1] in free) unlockCells(x - 1, y - 1)
                if (y > 0 && x < width - 1 && arr[(y - 1) * width + x + 1] in free) unlockCells(x + 1, y - 1)
                if (y < height - 1 && x > 0 && arr[(y + 1) * width + x - 1] in free) unlockCells(x - 1, y + 1)
                if (y < height - 1 && x < width - 1 && arr[(y + 1) * width + x + 1] in free) unlockCells(x + 1, y + 1)
            }
        }

        fun run(x: Int, y: Int, str: String) {
            if (str == "free") {
                if (arr[y * width + x] == 'X') { isMineChosen = true }
                else if (arr[y * width + x] == '+') { unlockCells(x, y) }
                else if (arr[y * width + x] == '*') { isMineChosen = true }
            } else {
                if (arr[y * width + x] == 'X') { arr[y * width + x] = '*' ; --this.minesLeft }
                else if (arr[y * width + x] == '*') { arr[y * width + x] = 'X' ; ++this.minesLeft }
                else if (arr[y * width + x] == '+') { arr[y * width + x] = '%' }
                else if (arr[y * width + x] == '%') { arr[y * width + x] = '+' }
            }
        }

        fun isContinued() = (if (arr.count { it in arrayOf('+', '%') } == this.mines) false
                                else !isMineChosen) && this.minesLeft > 0

        fun display(): String {
            val arrWidthFirst = Array(width){ if ((it + 1) / 100 == 0) '0' else Character.forDigit((it + 1) / 100, 10) }
            val arrWidthSecond = Array(width){ if ((it + 1) / 10 == 0) '0' else Character.forDigit((it + 1) / 10 % 10, 10) }
            val arrWidthThird = Array(width){ (it + 1) % 10 }
            var str = ""
            str += if (arrWidthFirst.count { it == '1' } > 0) "−−│${arrWidthFirst.joinToString("")}│\n" else ""
            str += if (arrWidthSecond.count { it == '1' } > 0) "−−│${arrWidthSecond.joinToString("")}│\n" else ""
            str += """
                −−│${arrWidthThird.joinToString("")}│
                −−│${Array(width){'='}.joinToString("")}│
                """.trimIndent()
            for (i in 0 until height) {
                str += "\n${String.format("%02d", i + 1)}│"
                for (n in 0 until width) {
                    str += if (isMineChosen) {
                        if (arr[i * width + n] == '%') '*' else if (arr[i * width + n] == '*') 'X'  else arr[i * width + n]
                    } else if (arr[i * width + n] !in arrayOf('X', '%')) arr[i * width + n]
                    else if (arr[i * width + n] == '%') '*'
                    else '+'
                }
                str += "│\n"
            }
            str += "−−│${Array(width){'='}.joinToString("")}│\n"
            return str
        }
    }
}