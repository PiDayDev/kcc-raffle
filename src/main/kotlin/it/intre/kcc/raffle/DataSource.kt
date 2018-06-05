package it.intre.kcc.raffle

interface DataSource {

    fun getAttendees(): List<Attendee>

    fun getPrizes(): List<Prize>

}