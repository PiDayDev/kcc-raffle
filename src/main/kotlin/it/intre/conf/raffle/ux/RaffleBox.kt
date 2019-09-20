package it.intre.conf.raffle.ux

import it.intre.conf.raffle.FONT_HEIGHT_TITLE
import it.intre.conf.raffle.H
import it.intre.conf.raffle.W
import it.intre.conf.raffle.asImage
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

sealed class RaffleBox : VBox() {
    init {
        minWidth(4 * W)
        alignment = Pos.CENTER
    }
}

class RaffleTitle(text: String = "", fontSize: Double = FONT_HEIGHT_TITLE) : RaffleBox() {
    private val textNode = Text(text)
    var text: String
        get() = textNode.text
        set(value) {
            textNode.text = value
        }

    init {
        textNode.textAlignment = TextAlignment.CENTER
        textNode.font = Font.font("Brandon Grotesque", FontWeight.BOLD, fontSize)
        children.add(textNode)
    }
}

class RaffleImage(imageFileName: String) : RaffleBox() {
    init {
        val imageView = ImageView(
                try {
                    imageFileName.asImage()
                } catch (e: Exception) {
                    System.err.println("Invalid image file $imageFileName")
                    "ghost.png".asImage()
                }
        ).apply {
            fitHeight = 4 * H
            isPreserveRatio = true
        }
        children.add(imageView)
    }
}