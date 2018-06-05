package it.intre.kcc.raffle

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