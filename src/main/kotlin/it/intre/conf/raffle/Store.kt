package it.intre.conf.raffle

data class Store(
        val attendees: MutableList<Attendee>,
        val prizes: MutableList<Prize>
) {
    constructor(dataSource: DataSource) : this(dataSource.getAttendees().toMutableList(), dataSource.getPrizes().toMutableList())
}

