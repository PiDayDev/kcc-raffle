package it.intre.kcc.raffle

data class Store(
        val attendees: List<Attendee>,
        val prizes: List<Prize>
) {
    constructor(dataSource: DataSource) : this(dataSource.getAttendees(), dataSource.getPrizes())

    operator fun minus(result: Result) = Store(attendees - result.winner, prizes - result.prize)
}

data class Result(
        val winner: Attendee = NOBODY,
        val prize: Prize = NONE
)