package com.example.myminesweeper

import kotlin.random.Random

fun Array<Char>.fillRandom(num: Int, capacity: Int) {
    val rand = Random.Default
    repeat(num) {
        while (true) {
            val res = rand.nextInt(0, capacity)
            if (this[res] == '+') { this[res] = 'X' ; break }
        }
    }
}