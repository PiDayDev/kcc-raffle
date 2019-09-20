package it.intre.conf.raffle

data class Result(
        val winner: Attendee = NOBODY,
        val prize: Prize = NONE
)