package com.example.demo

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.scene.input.KeyCode.*


class TetrisUI : Application() {
    private val game = TetrisGame()
    override fun start(mainWindow: Stage) {

    }
}

fun main() {
    Application.launch(TetrisUI::class.java)
}