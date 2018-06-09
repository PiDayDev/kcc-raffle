package it.intre.kcc.raffle

import javafx.application.Application
import javafx.application.Application.launch
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.stage.Stage
import javafx.util.Callback
import org.slf4j.LoggerFactory

/**
 * @author Damiano Salvi, Intré Srl
 */
class RaffleApp : Application() {

    override fun start(stage: Stage) {
        stage.title = "Milan Kotlin Community Conf 2018 raffle"
        stage.icons.add(ICON.asImage())

        stage.welcome()
        stage.show()
    }
}

const val ICON = "logo.png"
const val W = 800 * 1.618 / 4
const val H = 800.0 / 8
const val H1 = H * 0.80
const val H3 = H * 0.45
const val PAD = 10.0

val engine = TrulyRandomEngine(Store(CsvDataSource()))

val results = mutableListOf<Result>()

val logger = LoggerFactory.getLogger("KCC")!!

private fun Stage.welcome() {
    scene = KccScene {
        KccImageGrid(
                "#KCC18",
                ICON,
                "raffle",
                KccButton("LET'S GO !") { nextPrize() }
        )
    }
}

private fun Stage.nextPrize() {
    val prize = engine.nextPrize()
    log("+++ Next prize is: $prize")
    if (prize == NONE) {
        return recap()
    }
    scene = KccScene {
        KccImageGrid(
                prize.name,
                prize.image,
                prize.descr,
                KccButton("DRAW !") { nextWinner(prize) }
        )
    }
}

private fun Stage.nextWinner(prize: Prize) {
    val winner = engine.nextWinner()
    scene = KccScene {
        KccImageGrid(
                "$winner",
                prize.image,
                "$prize",
                KccButton("REDRAW", "no.png".asImage()) {
                    log("--- Prize was rejected by $winner")
                    nextWinner(prize)
                },
                KccButton("OK", "yes.png".asImage()) {
                    save(winner, prize)
                    nextPrize()
                }
        )
    }
}

private fun Stage.recap() {
    scene = KccScene {
        KccGrid {
            add(KccTitle("Winners"), 0, 0, 2, 1)
            val tv = KccTableView {
                columns.addAll(
                        column("#", 5) { "${it.pos}" },
                        column("Winner", 40) { "${it.winner}" },
                        column("Prize", 54) { "${it.prize}" }
                )
                items = FXCollections.observableArrayList(results)
                minWidth = 3 * W
                minHeight = 6 * H
            }
            add(tv, 0, 1, 7, 2)
        }
    }
}

class KccScene(root: () -> Parent) : Scene(root(), 4 * W + 2 * PAD, 8 * H + 2 * PAD)

open class KccGrid(builder: GridPane.() -> Unit = {}) : GridPane() {
    init {
        alignment = Pos.CENTER
        hgap = PAD
        vgap = PAD
        padding = Insets(PAD, PAD, PAD, PAD)
        background = Background(BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY))
        apply(builder)
    }
}

class KccImageGrid(title: String = "#KCC18",
                   image: String = ICON,
                   descr: String = "",
                   vararg buttons: KccButton) : KccGrid() {
    init {
        columnConstraints.addAll(constraints(50, 50))
        add(KccTitle(title), 0, 0, 2, 1)
        add(KccImage(image), 0, 1, 4, 4)
        add(KccTitle(descr), 0, 5, 2, 2)
        buttons.forEachIndexed { index, kccButton ->
            val colspan = 2 / buttons.size
            kccButton.minWidth = colspan * 2 * W
            add(kccButton, index, 7, colspan, 1)
        }
    }

}

sealed class KccBox : VBox() {
    init {
        minWidth(2 * W)
        alignment = Pos.CENTER
    }
}

class KccTitle(text: String = "") : KccBox() {
    init {
        val txt = Text(text)
        txt.textAlignment = TextAlignment.CENTER
        txt.font = Font.font("Brandon Grotesque", FontWeight.BOLD, H1)
        children.add(txt)
    }
}

class KccButton(text: String = "", image: Image? = null, action: (Any) -> Unit) : Button(text) {
    init {
        if (image != null) graphic = ImageView(image)
        textAlignment = TextAlignment.CENTER
        font = Font.font("Brandon Grotesque", FontWeight.NORMAL, H3)
        setOnAction(action)
    }
}

class KccImage(imageFileName: String) : KccBox() {
    init {
        children.add(ImageView(
                try {
                    imageFileName.asImage()
                } catch (e: Exception) {
                    System.err.println("Invalid image file $imageFileName")
                    ICON.asImage()
                }
        ))
    }
}

class KccTableView(builder: KccTableView.() -> Unit) : TableView<Result>() {
    init {
        apply(builder)
    }

    fun column(title: String, percWidth: Int = 0, f: (Result) -> String): KccTableColumn {
        val col = KccTableColumn(title, f)
        col.prefWidthProperty().bind(widthProperty().multiply(percWidth * 0.01))
        return col
    }
}

class KccTableColumn(title: String, f: (Result) -> String) : TableColumn<Result, String>(title) {
    init {
        cellValueFactory = Callback { param -> ReadOnlyObjectWrapper<String>(f(param.value)) }
    }
}


fun String.asImage() = Image(RaffleApp::class.java.getResourceAsStream("/${this}"))

fun constraints(vararg percentWidths: Int) = percentWidths.map { percent ->
    ColumnConstraints().apply { percentWidth = percent.toDouble() }
}

fun log(s: String) = logger.debug(s)

fun save(winner: Attendee, prize: Prize) {
    results.add(Result(winner, prize))
    log(">>> $winner has won $prize")
    logger.trace(">>> Mail: ${winner.mail} | Prize secret: ${prize.secret}")
}

fun main(args: Array<String>) {
    launch(RaffleApp::class.java)
}
