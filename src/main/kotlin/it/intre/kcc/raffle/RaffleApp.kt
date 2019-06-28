package it.intre.kcc.raffle

import javafx.animation.Transition
import javafx.application.Application
import javafx.application.Application.launch
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableCell
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
import javafx.util.Duration
import org.slf4j.LoggerFactory
import java.text.Normalizer

/**
 * @author Damiano Salvi, Intré Srl
 */
class RaffleApp : Application() {

    override fun start(stage: Stage) {
        stage.title = "Working Software 2019 raffle"
        stage.icons.add(ICON.asImage())

        stage.welcome()
        stage.show()
    }
}

const val ICON = "logo.png"
const val W = 800 * 1.618 / 4
const val H = 800.0 / 8
const val FONT_HEIGHT_TITLE = H * 0.80
const val FONT_HEIGHT_NOTES = H * 0.50
const val FONT_HEIGHT_RECAP = H * 0.185
const val FONT_HEIGHT_BUTTON = H * 0.45
const val PAD = 10.0

val engine = TrulyRandomEngine(Store(CsvDataSource()))

val results = mutableListOf<Result>()

val logger = LoggerFactory.getLogger("KCC")!!

private fun Stage.welcome() {
    scene = KccScene {
        KccImageGrid(
                "Working Software",
                ICON,
                "#WSC2019 raffle",
                "We have a staggering total of ${engine.store.prizes.size} prizes!",
                false,
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
                "Prize #${prize.pos}",
                prize.image,
                prize.name,
                prize.descr,
                false,
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
                prize.name,
                prize.descr,
                true,
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
                        column("#", 5) { "${it.prize.pos}" },
                        column("Winner", 27) { "${it.winner}" },
                        column("Prize", 67) { "${it.prize}" }
                )
                items = FXCollections.observableArrayList(results.sortedBy { it.prize.pos })
                minWidth = 4 * W
                minHeight = 7 * H
            }
            add(tv, 0, 1, 7, 2)
        }
    }
}

class KccScene(root: () -> Parent) : Scene(root(), 4 * W + 2 * PAD, 8 * H + 2 * PAD)

open class KccGrid(builder: GridPane.() -> Unit = {}) : GridPane() {
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

class KccImageGrid(title: String,
                   image: String,
                   note1: String,
                   note2: String,
                   animate: Boolean,
                   vararg buttons: KccButton) : KccGrid() {
    init {
        val row1 = KccTitle("")
        add(row1, 0, 0, 2, 1)
        if (animate) {
            KccTransition(row1, title).play()
        } else {
            row1.text = title
        }
        add(KccImage(image), 0, 1, 4, 4)
        add(KccTitle(note1, FONT_HEIGHT_NOTES), 0, 5, 2, 1)
        add(KccTitle(note2, FONT_HEIGHT_NOTES), 0, 6, 2, 1)
        val count = buttons.size
        val colSpan = 2 / count
        buttons.forEachIndexed { index, button ->
            button.minWidth = colSpan * 2 * W
            add(button, index, 7, colSpan, 1)
        }
    }
}

class KccTransition(private val element: KccTitle, private val text: String) : Transition() {
    init {
        cycleDuration = Duration.millis(642.0)
    }

    override fun interpolate(frac: Double) {
        element.text = if (frac >= 1.0) text else text.randomize(frac)
    }
}

sealed class KccBox : VBox() {
    init {
        minWidth(4 * W)
        alignment = Pos.CENTER
    }
}

class KccTitle(text: String = "", fontSize: Double = FONT_HEIGHT_TITLE) : KccBox() {
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

class KccButton(text: String = "", image: Image? = null, action: (Any) -> Unit) : Button(text) {
    init {
        if (image != null) graphic = ImageView(image)
        textAlignment = TextAlignment.CENTER
        font = Font.font("Brandon Grotesque", FontWeight.NORMAL, FONT_HEIGHT_BUTTON)
        cursor = Cursor.HAND
        setOnAction(action)
    }
}

class KccImage(imageFileName: String) : KccBox() {
    init {
        val imageView = ImageView(
                try {
                    imageFileName.asImage()
                } catch (e: Exception) {
                    System.err.println("Invalid image file $imageFileName")
                    ICON.asImage()
                }
        ).apply {
            fitHeight = 4 * H
            isPreserveRatio = true
        }
        children.add(imageView)
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
        cellFactory = Callback<TableColumn<Result, String>, TableCell<Result, String>> { KccTableCell() }
    }
}

class KccTableCell : TableCell<Result, String>() {
    override fun updateItem(item: String?, empty: Boolean) {
        if (item != null) {
            text = item
            font = Font.font("Brandon Grotesque", FontWeight.NORMAL, FONT_HEIGHT_RECAP)
        }
    }
}

fun String.asImage() = Image(RaffleApp::class.java.getResourceAsStream("/${this}"))

fun String.randomize(frac: Double) =
        Normalizer.normalize(this, Normalizer.Form.NFC)
                .toLowerCase().toList().shuffled()
                .take((frac * (2 - frac) * length).toInt()).joinToString("").split(" ")
                .joinToString(" ") { it.capitalize() }

fun log(s: String) = logger.debug(s)

fun save(winner: Attendee, prize: Prize) {
    results.add(Result(winner, prize))
    log(">>> $winner has won $prize")
    logger.trace(">>> Mail: ${winner.mail} | Prize secret: ${prize.secret}")
}

fun main() {
    launch(RaffleApp::class.java)
}
