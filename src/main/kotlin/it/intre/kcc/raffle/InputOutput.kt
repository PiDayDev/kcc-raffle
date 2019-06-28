package it.intre.kcc.raffle

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.TextArea
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.stage.Stage
import kotlin.concurrent.thread


interface InputOutput {

    fun read():Char

    fun print(result: Result) = print(result.winner, result.prize)

    fun print(attendee: Attendee, prize: Prize)
}

infix fun Attendee.wins(prize: Prize) = """
    ------------------------------------------
    $this
    wins
    $prize!
    ------------------------------------------

    """.trimIndent()

class StdInOut : InputOutput {

    override fun print(attendee: Attendee, prize: Prize) {
        println(attendee wins prize)
    }

    override fun read(): Char = System.`in`.read().toChar()

}

class Panel : Application() {

    private val size = 30
    private val textArea = TextArea("Hello")

    override fun start(stage: Stage) {
        val d = 22.0 * size + 98.5
        textArea.setPrefSize(d, d)
        textArea.font = Font("Courier New", 20.0)
        val pane = Pane(textArea)
        pane.setPrefSize(d, d)
        val scene = Scene(pane, d, d)
        stage.scene = scene

        stage.show()
        thread(start = true, block = {
            textArea.text = "Hello"
            Thread.sleep(3003)
        })
    }

    infix fun shows(s: String) {
        textArea.text = s
    }

}

class WindowOutput : InputOutput {
    private val panel: Panel

    init {
        object : Thread() {
            override fun run() {
                Application.launch(Panel::class.java)
            }
        }.start()
        panel = Panel()
    }

    override fun print(attendee: Attendee, prize: Prize) {
        panel shows (attendee wins prize)
    }

    override fun read(): Char = System.`in`.read().toChar()
}