package it.intre.kcc.raffle

sealed class Output {

    fun print(result: Result) = print(result.winner, result.prize)

    abstract fun print(attendee: Attendee, prize: Prize)
}

class StdOutput : Output() {

    override fun print(attendee: Attendee, prize: Prize) {
        println("""
------------------------------------------
${attendee.firstName} ${attendee.lastName}
wins
${prize.name}! (${prize.details})
------------------------------------------

""".trimIndent())
        }

}