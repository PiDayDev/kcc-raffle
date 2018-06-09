package it.intre.kcc.raffle

import java.security.SecureRandom

abstract class Engine(var store: Store) {

    fun hasPrizes() = store.prizes.isNotEmpty()
    fun hasAttendees() = store.attendees.isNotEmpty()

    fun nextPrize(): Prize = if (hasPrizes()) store.prizes.removeAt(0) else NONE
    abstract fun nextWinner(): Attendee

}

class ZeroSuspenseEngine(store: Store) : Engine(store) {

    override fun nextWinner(): Attendee = store.attendees.removeAt(0)

}

class TrulyRandomEngine(store: Store) : Engine(store) {

    private val rnd = SecureRandom()

    override fun nextWinner(): Attendee = if (hasAttendees()) store.attendees.removeAtRandom() else NOBODY

    private fun <T> MutableList<T>.removeAtRandom() = removeAt(rnd.nextInt(size))

}
