package com.example.demo

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.scene.input.KeyCode.*


class TetrisUI : Application() {

    private val game = TetrisGame()

    override fun start(mainWindow: Stage) {
        val canvas = Canvas(TetrisGame.BLOCK_SIZE * TetrisGame.WIDTH, TetrisGame.BLOCK_SIZE * TetrisGame.HEIGHT)
        val gc = canvas.graphicsContext2D
        val borderPane = BorderPane(canvas)

        val timer = object : AnimationTimer() {
            override fun handle(now: Long) {
                if (now - game.lastMove > TetrisGame.SPEED) {
                    game.lastMove = now
                    game.update()
                    game.draw(gc)
                }
            }

        }
        timer.start()

        val scene = Scene(borderPane)
        mainWindow.scene = scene
        mainWindow.title = "Tetris"
        mainWindow.show()

        scene.setOnKeyPressed { click ->
            when (click.code) {
                LEFT -> game.moveLeft()
                RIGHT -> game.moveRight()
                DOWN -> game.moveDown()
                UP -> game.rotatePiece()
                else -> {
                    val alert = Alert(Alert.AlertType.WARNING)
                    alert.title = "Error"
                    alert.contentText = "Incorrect key: ${click.code} Use keys only for game"
                    alert.showAndWait()
                }
            }
        }
    }
}

fun main() {
    Application.launch(TetrisUI::class.java)
}