package it.intre.conf.raffle.ux

import it.intre.conf.raffle.FONT_HEIGHT_RECAP
import it.intre.conf.raffle.Result
import javafx.scene.control.TableCell
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class RaffleTableCell : TableCell<Result, String>() {
    override fun updateItem(item: String?, empty: Boolean) {
        if (item != null) {
            text = item
            font = Font.font("Brandon Grotesque", FontWeight.NORMAL, FONT_HEIGHT_RECAP)
        }
    }
}