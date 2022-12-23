//package com.example.myminesweeper
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import kotlin.random.Random
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import java.util.*
//
//class PlayActivity : AppCompatActivity() {
//
//    private fun setToast(str: String) = Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
//
//    private fun isCorrectInput(arrSet: List<String>, arr: Array<Char>, width: Int): Boolean {
//        val x = arrSet[0].toInt()
//        val y = arrSet[1].toInt()
//        val validStr = arrayOf("mine", "free")
//        return if (arrSet.size != 3 || x !in 1..10 || y !in 1..10
//            || arrSet[2] !in validStr) { setToast("Parameters are not valid, type again") ; false }
//        else when (arr[(y - 1) * width + x - 1]) {
//            in '1'..'8' -> { setToast("There is a number in this cell!") ; false }
//            '0' -> { setToast("The cell is already free!"); false }
//            else -> true
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_play)
//
//        val setterText = findViewById<TextView>(R.id.setter_text)
//        val ground = findViewById<TextView>(R.id.game)
//        val arr = intent.getIntArrayExtra("values")
//        if (arr == null) ground.text = "Error while sending intent"
//        else {
//            val game = Minesweeper(arr[0], arr[1], arr[2])
//            ground.text = game.display()
//            val check = findViewById<Button>(R.id.button_play)
//            check.setOnClickListener {
//                val setter = findViewById<TextView>(R.id.setter_edit)
//                val arrSet = setter.text.toString().split(' ').map { it.lowercase(Locale.ROOT) }
//                setter.text = ""
//                if (isCorrectInput(arrSet, game.getArr(), game.getWidth())) {
//                    game.run(arrSet[0].toInt() - 1, arrSet[1].toInt() - 1, arrSet[2])
//                    ground.text = game.display()
//                    if (!game.isContinued()) {
//                        if (game.isPlayerLost()) {
//                            setterText.setTextColor(ContextCompat.getColor(this, R.color.red))
//                            setterText.text = "You stepped on a mine and failed!"
//                        } else {
//                            setterText.setTextColor(ContextCompat.getColor(this, R.color.teal_700))
//                            setterText.text = "Congratulations! You found all the mines!"
//                        }
//                        check.setText("BACK TO MAIN")
//                        check.setOnClickListener {
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}