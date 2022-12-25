package com.example.myminesweeper

import kotlin.random.Random

fun Array<Char>.fillRandom(num: Int, capacity: Int, firstCell: Int) {
    val rand = Random.Default
    repeat(num) {
        while (true) {
            val res = rand.nextInt(0, capacity)
            if (res == firstCell) continue
            if (this[res] == '+') { this[res] = 'X' ; break }
        }
    }
}