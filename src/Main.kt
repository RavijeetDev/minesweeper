package minesweeper

import java.awt.Point
import kotlin.random.Random

const val ROWS = 9
const val COLUMNS = 9

private val map = MutableList(ROWS) {
    MutableList(COLUMNS) { '.' }
}

enum class GameState {
    RUNNING, FAILED, COMPLETED
}

private val minesPositions = arrayListOf<Point>()
private val numberPositions = hashMapOf<Point, Int>()
private val markedPositions = arrayListOf<Point>()
private val exploreCoordinates = arrayListOf<ArrayList<Int>>()

private var gameFinished = false
private var gameState = GameState.RUNNING


fun main() {
    setUpGame()
    playGame()
}

fun setUpGame() {
    plotMines()
    hideMines()
    hideNumbers()
    printMap()
}

fun plotMines() {

    println("How many mines do you want on the field?")

    val noOfMines = readln().toInt()

    repeat(noOfMines) {
        val row = Random.nextInt(9)
        val column = Random.nextInt(9)
        addMineToMap(row, column)
    }
}

fun addMineToMap(row: Int, column: Int) {
    if (map[row][column] == 'X') {
        val row = Random.nextInt(9)
        val column = Random.nextInt(9)
        addMineToMap(row, column)
    } else {
        map[row][column] = 'X'
        minesPositions.add(Point(column, row))
        plotNumbersNearMine(row, column)
    }
}

fun plotNumbersNearMine(row: Int, column: Int) {
    for (r in maxOf(row - 1, 0)..minOf(row + 1, 8)) {
        for (c in maxOf(column - 1, 0)..minOf(column + 1, 8)) {
            if (map[r][c] != 'X') {
                map[r][c] = (map[r][c].intValue() + 1).digitToChar()
                numberPositions[Point(c, r)] = numberPositions.getOrDefault(Point(c, r), 0) + 1
            }
        }
    }
}

fun hideMines() {
    for (row in map) {
        row.replaceAll {
            if (it == 'X') '.'
            else it
        }
    }
}

fun hideNumbers() {
    printMap()
    for (row in map) {
        row.replaceAll {
            if (it in '1'..'8') '.'
            else it
        }
    }
//    println(numberPositions)
}

fun playGame() {
    do {
        println("Set/unset mine marks or claim a cell as free:")
        val (x, y, command) = readln().split(" ")

        val row = y.toInt() - 1
        val column = x.toInt() - 1

        when (command) {
            "mine" -> markCell(row, column)
            "free" -> exploreCell(row, column)
        }

        if (gameState != GameState.FAILED) checkIfGameIsFinished()

    } while (gameState == GameState.RUNNING)

    if (gameState == GameState.COMPLETED)
        println("Congratulations! You found all the mines!")
    else if (gameState == GameState.FAILED)
        println("You stepped on a mine and failed!")
}

fun markCell(row: Int, column: Int) {
    when (map[row][column]) {
        '.' -> {
            map[row][column] = '*'
            markedPositions.add(Point(column, row))
            printMap()
        }

        '*' -> {
            map[row][column] = '.'
            markedPositions.remove(Point(column, row))
            printMap()
        }

        else -> {
            println("There is a number here!")
        }
    }
}

fun exploreCell(row: Int, column: Int) {
    if (minesPositions.contains(Point(column, row))) {
        gameState = GameState.FAILED
        for (loc in minesPositions) {
            map[loc.y][loc.x] = 'X'
        }
        printMap()

    } else if (numberPositions.containsKey(Point(column, row))) {
        map[row][column] = numberPositions[Point(column, row)]!!.digitToChar()
        printMap()

    } else if (map[row][column] == '.') {
        map[row][column] = '/'
//        exploreCoordinates.add(arrayListOf(row, column))
        revealNeighbourCells(row, column)
        printMap()
    }
}

fun revealNeighbourCells(row: Int, column: Int) {
    for (r in maxOf(row - 1, 0)..minOf(row + 1, 8)) {
        for (c in maxOf(column - 1, 0)..minOf(column + 1, 8)) {
            if (r != row || c != column) {
//                if (map[r][c] in '1'..'8') {
//                    map[r][c] = numberPositions.get(Point(c, r))!!.digitToChar()
//                } else if (map[r][c] == '.') {
//                    map[r][c] = '/'
//                    revealNeighbourCells(r, c)
//                }

                if (numberPositions.containsKey(Point(c, r))) {
                    map[r][c] = numberPositions[Point(c, r)]!!.digitToChar()
                } else if (map[r][c] == '.' || map[r][c] == '*') {
                    map[r][c] = '/'
                    revealNeighbourCells(r, c)
                }
            }
        }
    }
}

fun checkIfGameIsFinished() {
    if (markedPositions.containsAll(minesPositions) || ifUnexploredCellsAreMines()) {
        gameState = GameState.COMPLETED
    }
}

fun ifUnexploredCellsAreMines(): Boolean {
    var gameFinished = false
    val unexploredCells = arrayListOf<Point>()
    for (row in map.indices) {
        for (column in map[row].indices) {
            if (map[row][column] == '.') {
                unexploredCells.add(Point(column, row))
            }
        }
    }
    if (minesPositions.containsAll(unexploredCells)) gameFinished = true

    return gameFinished
}

fun printMap() {
    println()
    println(" |123456789|")
    println("-|---------|")
    var rowNum = 0
    for (row in map) {
        rowNum++
        println("$rowNum|${row.joinToString("")}|")
    }
    println("-|---------|")
}

fun Char.intValue(): Int {
    return if (this == '.') 0
    else this.code - 48
}
