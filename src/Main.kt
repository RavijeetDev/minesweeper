package minesweeper

import kotlin.random.Random

fun main() {
    val map = mutableListOf(
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
        mutableListOf('.','.','.','.','.','.','.','.','.'),
    )

    plotMines(map)

    printMap(map)
}

fun plotMines(map: MutableList<MutableList<Char>>) {

    println("How many mines do you want on the field?")

    val noOfMines = readln().toInt()

    repeat(noOfMines) {
        val row = Random.nextInt(9)
        val column = Random.nextInt(9)
        addMineToMap(row, column, map)
    }
}

fun addMineToMap(row: Int, column: Int, map: MutableList<MutableList<Char>>) {
    if (map[row][column] == 'X') {
        val row = Random.nextInt(9)
        val column = Random.nextInt(9)
        addMineToMap(row, column, map)
    } else {
        map[row][column] = 'X'
    }
}

fun printMap(map: MutableList<MutableList<Char>>) {
    for (row in map) {
        println(row.joinToString(""))
    }
}
