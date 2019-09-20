package it.intre.conf.raffle.ux

import it.intre.conf.raffle.FONT_HEIGHT_NOTES
import it.intre.conf.raffle.RaffleTransition
import it.intre.conf.raffle.W

class RaffleImageGrid(title: String,
                      image: String,
                      note1: String,
                      note2: String,
                      animate: Boolean,
                      vararg buttons: RaffleButton) : RaffleGrid() {
    init {
        val row1 = RaffleTitle("")
        add(row1, 0, 0, 2, 1)
        if (animate) {
            RaffleTransition(row1, title).play()
        } else {
            row1.text = title
        }
        add(RaffleImage(image), 0, 1, 4, 4)
        add(RaffleTitle(note1, FONT_HEIGHT_NOTES), 0, 5, 2, 1)
        add(RaffleTitle(note2, FONT_HEIGHT_NOTES), 0, 6, 2, 1)
        val count = buttons.size
        val colSpan = 2 / count
        buttons.forEachIndexed { index, button ->
            button.minWidth = colSpan * 2 * W
            add(button, index, 7, colSpan, 1)
        }
    }
}