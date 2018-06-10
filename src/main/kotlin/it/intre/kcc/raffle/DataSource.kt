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
            Prize(4,"Lorem", "A", "img.jpg"),
            Prize(3,"Lorem", "B", "img.jpg"),
            Prize(2,"Lorem", "C", "img.jpg"),
            Prize(1,"Ipsum", "nice", "img.jpg")
    )
}

class CsvDataSource : DataSource {

    override fun getAttendees(): List<Attendee> = list("attendees.csv").map { Attendee(it[0], it[1], it[2]) }

    override fun getPrizes(): List<Prize> = list("prizes.csv").map { Prize(it[0].trim().toIntOrNull() ?: 0, it[1], it[2], it[3], it[4]) }

    private fun list(file: String): List<List<String>> = File(ClassLoader.getSystemResource(file).file)
            .readLines()
            .drop(1) // field names
            .distinct() // no duplicates
            .map { it.split(",") }

}
