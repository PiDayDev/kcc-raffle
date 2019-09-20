package it.intre.conf.raffle.ux

import it.intre.conf.raffle.Result
import javafx.scene.control.TableView

class RaffleTableView(builder: RaffleTableView.() -> Unit) : TableView<Result>() {
    init {
        apply(builder)
    }

    fun column(title: String, percWidth: Int = 0, f: (Result) -> String): RaffleTableColumn {
        val col = RaffleTableColumn(title, f)
        col.prefWidthProperty().bind(widthProperty().multiply(percWidth * 0.01))
        return col
    }
}