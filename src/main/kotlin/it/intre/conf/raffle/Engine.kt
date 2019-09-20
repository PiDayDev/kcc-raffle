package it.intre.conf.raffle

import java.security.SecureRandom

/** Raffle engine: can return next prize and next winner */
sealed class Engine(var store: Store) {

    fun hasPrizes() = store.prizes.isNotEmpty()
    fun hasAttendees() = store.attendees.isNotEmpty()

    fun nextPrize(): Prize = if (hasPrizes()) store.prizes.removeAt(0) else NONE
    abstract fun nextWinner(): Attendee

}

/** Always return the next entry in order */
class ZeroSuspenseEngine(store: Store) : Engine(store) {

    override fun nextWinner(): Attendee = store.attendees.removeAt(0)

}

/** Return a random entry not already returned */
class TrulyRandomEngine(store: Store) : Engine(store) {

    private val rnd = SecureRandom()

    override fun nextWinner(): Attendee = if (hasAttendees()) store.attendees.removeAtRandom() else NOBODY

    private fun <T> MutableList<T>.removeAtRandom() = removeAt(rnd.nextInt(size))

}
