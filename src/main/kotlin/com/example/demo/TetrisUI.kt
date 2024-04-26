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
                    with(game) {
                        lastMove = now
                        update()
                        draw(gc)
                    }
                }
            }
        }
        timer.start()

        with(mainWindow) {
            scene = Scene(borderPane)
            title = "Tetris"
            show()
        }

        mainWindow.scene.setOnKeyPressed { event ->
            when (event.code) {
                LEFT -> game.moveLeft()
                RIGHT -> game.moveRight()
                DOWN -> game.moveDown()
                UP -> game.rotatePiece()
                else -> {
                    with(Alert(Alert.AlertType.WARNING)) {
                        title = "Error"
                        contentText = "Incorrect key: ${event.code} Use keys only for game"
                        showAndWait()
                    }
                }
            }
        }
    }
}

fun main() {
    Application.launch(TetrisUI::class.java)
}