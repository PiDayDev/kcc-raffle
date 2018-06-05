package it.intre.kcc.raffle

interface Engine {

    fun drawPrize(): Pair<Attendee, Prize>

}