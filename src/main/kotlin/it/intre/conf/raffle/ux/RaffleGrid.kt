package it.intre.conf.raffle.ux

import it.intre.conf.raffle.PAD
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.*
import javafx.scene.paint.Paint

open class RaffleGrid(builder: GridPane.() -> Unit = {}) : GridPane() {
    init {
        columnConstraints.addAll(intArrayOf(50, 50).map { percent ->
            ColumnConstraints().apply { percentWidth = percent.toDouble() }
        })
        alignment = Pos.CENTER
        hgap = PAD
        vgap = PAD
        padding = Insets(PAD)
        background = Background(BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY))
        apply(builder)
    }
}