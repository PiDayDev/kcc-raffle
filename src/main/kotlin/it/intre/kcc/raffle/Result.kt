package it.intre.kcc.raffle

data class Result(
        val winner: Attendee = NOBODY,
        val prize: Prize = NONE
)