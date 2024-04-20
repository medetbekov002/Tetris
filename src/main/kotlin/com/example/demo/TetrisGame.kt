package com.example.demo

import javafx.geometry.Point2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.util.*

class TetrisGame {
    companion object {
        const val BLOCK_SIZE = 30.0
        const val WIDTH = 10
        const val HEIGHT = 20
        const val SPEED = 500_000_000 // 0.5 sec
    }

    private val colors = arrayOf(Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.ORANGE, Color.PURPLE)
    private val board = Array(HEIGHT) { BooleanArray(WIDTH) }
    private var currentPiece: Array<Point2D>? = null
    private var currentPieceColor: Color? = null
    private var currentX = 0
    private var currentY = 0
    var lastMove = 0L

    init {
        reset()
    }

    fun update() {
        if (currentPiece == null) spawnPiece()
        else if (validMove(currentPiece!!, currentX, currentY + 1)) currentY++
        else {
            for (p in currentPiece!!) {
                val x = currentX + p.x.toInt()
                val y = currentY + p.y.toInt()
                board[y][x] = true
            }
            clearLines()
            spawnPiece()
        }
    }

    fun moveLeft() {
        if (validMove(currentPiece!!, currentX - 1, currentY)) currentX--
    }

    fun moveRight() {
        if (validMove(currentPiece!!, currentX + 1, currentY)) currentX++
    }

    fun moveDown() {
        if (validMove(currentPiece!!, currentX, currentY + 1)) currentY++
    }

    fun rotatePiece() {
        val rotated = Array(currentPiece!!.size) { Point2D(0.0, 0.0) }
        for (i in currentPiece!!.indices) {
            val newX = -currentPiece!![i].y
            val newY = currentPiece!![i].x
            rotated[i] = Point2D(newX, newY)
        }
        if (validMove(rotated, currentX, currentY)) currentPiece = rotated
    }

    fun draw(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, BLOCK_SIZE * WIDTH, BLOCK_SIZE * HEIGHT)
        for (i in 0 until HEIGHT) {
            for (j in 0 until WIDTH) {
                if (board[i][j]) {
                    gc.fill = Color.BLACK
                    gc.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
                    gc.strokeRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
                }
            }
        }
        currentPiece?.forEach { p ->
            gc.fill = currentPieceColor
            gc.fillRect((currentX + p.x.toInt()) * BLOCK_SIZE, (currentY + p.y.toInt()) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
            gc.strokeRect((currentX + p.x.toInt()) * BLOCK_SIZE, (currentY + p.y.toInt()) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
        }
    }

    private fun validMove(piece: Array<Point2D>, x: Int, y: Int): Boolean {
        for (p in piece) {
            val newX = x + p.x.toInt()
            val newY = y + p.y.toInt()
            if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || board[newY][newX]) return false
        }
        return true
    }

    private fun reset() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = false
            }
        }
    }

    private fun spawnPiece() {
        val random = Random()
        val shape = random.nextInt(7)
        val color = colors[random.nextInt(colors.size)]
        currentPieceColor = color
        when (shape) {
            0 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(1.0, 0.0), Point2D(2.0, 0.0), Point2D(3.0, 0.0))
            1 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(0.0, 1.0), Point2D(1.0, 1.0), Point2D(1.0, 0.0))
            2 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(1.0, 0.0), Point2D(2.0, 0.0), Point2D(0.0, 1.0))
            3 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(1.0, 0.0), Point2D(2.0, 0.0), Point2D(2.0, 1.0))
            4 -> currentPiece = arrayOf(Point2D(0.0, 1.0), Point2D(1.0, 1.0), Point2D(1.0, 0.0), Point2D(2.0, 0.0))
            5 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(1.0, 0.0), Point2D(1.0, 1.0), Point2D(2.0, 1.0))
            6 -> currentPiece = arrayOf(Point2D(0.0, 0.0), Point2D(1.0, 0.0), Point2D(1.0, 1.0), Point2D(1.0, 2.0))
        }
        currentX = WIDTH / 2 - 2
        currentY = 0
    }

    private fun clearLines() {
        var numLines = 0
        for (i in HEIGHT - 1 downTo 0) {
            var lineFull = true
            for (j in 0 until WIDTH) {
                if (!board[i][j]) {
                    lineFull = false
                    break
                }
            }
            if (lineFull) {
                numLines++
                for (k in i downTo 1) {
                    for (j in 0 until WIDTH) {
                        board[k][j] = board[k - 1][j]
                    }
                }
                for (j in 0 until WIDTH) {
                    board[0][j] = false
                }
            }
        }
    }
}