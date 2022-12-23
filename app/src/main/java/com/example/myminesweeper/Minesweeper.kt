package com.example.myminesweeper

class Minesweeper(private var height: Int, private val width: Int = 0, private val mines: Int = 0) {
    private var capacity = width * height
    private val arr = Array(capacity){'+'}
    private var minesLeft = mines
    private var isMineChosen = false

    init { arr.fillRandom(this.mines, this.capacity) }

    fun getArr() = this.arr
    fun getWidth() = this.width
    fun getHeight() = this.height
    fun getCell(x: Int, y: Int) = arr[y * width + x]
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

    fun display(): CharArray {
        val mutList: MutableList<Char> = mutableListOf()
        for (i in 0 until height) {
            for (n in 0 until width) {
                mutList.add(if (isMineChosen) {
                    if (arr[i * width + n] == '%') '*' else if (arr[i * width + n] == '*') 'X'  else arr[i * width + n]
                } else if (arr[i * width + n] !in arrayOf('X', '%')) arr[i * width + n]
                else if (arr[i * width + n] == '%') '*'
                else '+')
            }
        }
        return mutList.toCharArray()
    }
}