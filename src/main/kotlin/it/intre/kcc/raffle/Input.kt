package it.intre.kcc.raffle

sealed class Input {

    abstract fun read():Char

}

class StdInput: Input() {

    override fun read(): Char = System.`in`.read().toChar()

}