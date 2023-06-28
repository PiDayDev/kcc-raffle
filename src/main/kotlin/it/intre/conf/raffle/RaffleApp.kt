package it.intre.conf.raffle

import it.intre.conf.raffle.ux.*
import javafx.animation.Transition
import javafx.application.Application
import javafx.application.Application.launch
import javafx.collections.FXCollections
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.util.Duration
import org.slf4j.LoggerFactory
import java.text.Normalizer

/**
 * @author Damiano Salvi, Intré Srl
 */
class RaffleApp : Application() {

    override fun start(stage: Stage) {
        stage.title = config.appTitle
        stage.icons.add(config.welcomeLogo.asImage())

        stage.welcome()
        stage.show()
    }
}

const val W = 1600.0 / 4
const val H = 900.0 / 8
const val FONT_HEIGHT_TITLE = H * 0.80
const val FONT_HEIGHT_NOTES = H * 0.50
const val FONT_HEIGHT_RECAP = H * 0.185
const val FONT_HEIGHT_BUTTON = H * 0.45
const val PAD = 10.0

val config = Config()

val engine = TrulyRandomEngine(Store(CsvDataSource))

val results = mutableListOf<Result>()

val logger = LoggerFactory.getLogger("raffle")!!

private fun Stage.welcome() {
    scene = RaffleScene {
        RaffleImageGrid(
            config.welcomeTitle,
            config.welcomeLogo,
            config.welcomeNote,
            "We have a staggering total of ${engine.store.prizes.size} prizes!",
            false,
            RaffleButton("LET'S GO !") { nextPrize() }
        )
    }
}

private fun Stage.nextPrize() {
    val prize = engine.nextPrize()
    log("+++ Next prize is: $prize")
    if (prize == NONE) {
        return recap()
    }
    scene = RaffleScene {
        RaffleImageGrid(
            "Prize #${prize.pos}",
            prize.image,
            prize.name,
            prize.descr,
            false,
            RaffleButton("DRAW !") { nextWinner(prize) }
        )
    }
}

private fun Stage.nextWinner(prize: Prize) {
    val winner = engine.nextWinner()
    scene = RaffleScene {
        RaffleImageGrid(
            "$winner",
            prize.image,
            prize.name,
            prize.descr,
            true,
            RaffleButton("REDRAW", "no.png".asImage()) {
                log("--- Prize was rejected by $winner")
                nextWinner(prize)
            },
            RaffleButton("OK", "yes.png".asImage()) {
                save(winner, prize)
                nextPrize()
            }
        )
    }
}

private fun Stage.recap() {
    scene = RaffleScene {
        RaffleGrid {
            add(RaffleTitle("Winners"), 0, 0, 2, 1)
            val tv = RaffleTableView {
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

class RaffleTransition(private val element: RaffleTitle, private val text: String) : Transition() {
    init {
        cycleDuration = Duration.millis(642.0)
    }

    override fun interpolate(frac: Double) {
        element.text = if (frac >= 1.0) text else text.randomize(frac)
    }
}

fun String.asImage() = try {
    Image(RaffleApp::class.java.getResourceAsStream("/${this}"))
} catch (e: Exception) {
    Image(RaffleApp::class.java.getResourceAsStream("/ghost.png"))
}

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
