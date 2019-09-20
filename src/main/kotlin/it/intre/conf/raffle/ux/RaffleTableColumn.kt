package it.intre.conf.raffle.ux

import it.intre.conf.raffle.Result
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback

class RaffleTableColumn(title: String, f: (Result) -> String) : TableColumn<Result, String>(title) {
    init {
        cellValueFactory = Callback { param -> ReadOnlyObjectWrapper<String>(f(param.value)) }
        cellFactory = Callback<TableColumn<Result, String>, TableCell<Result, String>> { RaffleTableCell() }
    }
}