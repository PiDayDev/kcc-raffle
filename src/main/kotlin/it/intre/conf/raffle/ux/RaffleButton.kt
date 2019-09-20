package it.intre.conf.raffle.ux

import it.intre.conf.raffle.FONT_HEIGHT_BUTTON
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment

class RaffleButton(text: String = "", image: Image? = null, action: (Any) -> Unit) : Button(text) {
    init {
        if (image != null) graphic = ImageView(image)
        textAlignment = TextAlignment.CENTER
        font = Font.font("Brandon Grotesque", FontWeight.NORMAL, FONT_HEIGHT_BUTTON)
        cursor = Cursor.HAND
        setOnAction(action)
    }
}