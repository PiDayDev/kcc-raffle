package it.intre.kcc.raffle

import java.io.File

interface DataSource {

    fun getAttendees(): List<Attendee>

    fun getPrizes(): List<Prize>

}

class MemoryDataSource : DataSource {

    override fun getAttendees(): List<Attendee> = listOf(
            Attendee("Arthur", "Dent", "dent42@mailinator.com"),
            Attendee("Ford", "Prefect", "prefect42@mailinator.com"),
            Attendee("Zaphod", "Beeblebrox", "beeblebrox42@mailinator.com"),
            Attendee("Tricia", "McMillan", "mcmillan42@mailinator.com")
    )

    override fun getPrizes(): List<Prize> = listOf(
            Prize("Lorem", "wow"),
            Prize("Ipsum", "nice")
    )
}

class CsvDataSource : DataSource {

    override fun getAttendees(): List<Attendee> = list("attendees.csv").map { Attendee(it[0], it[1], it[2]) }

    override fun getPrizes(): List<Prize> = list("prizes.csv").map { Prize(it[0], it[1]) }

    private fun list(file: String): List<List<String>> = File(ClassLoader.getSystemResource(file).file)
                .readLines()
                .drop(1) // field names
                .distinct() // no duplicates
                .map { it.split(",") }

}
