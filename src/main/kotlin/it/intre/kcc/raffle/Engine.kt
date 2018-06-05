package it.intre.kcc.raffle

import java.security.SecureRandom

abstract class Engine(val store: Store) {

    fun hasMorePrizes() = store.prizes.isNotEmpty() && store.attendees.isNotEmpty()

    abstract fun drawPrize(): Result

    abstract operator fun minus(result: Result): Engine
}

class ZeroSuspenseEngine(store: Store) : Engine(store) {

    override fun drawPrize(): Result = Result(store.attendees[0], store.prizes[0])

    override fun minus(result: Result) = ZeroSuspenseEngine(store - result)

}

class TrulyRandomEngine(store: Store) : Engine(store) {

    private val rnd = SecureRandom()

    override fun drawPrize(): Result = Result(store.attendees.random(), store.prizes[0])

    private fun <T> List<T>.random() = this[rnd.nextInt(size)]

    override fun minus(result: Result) = TrulyRandomEngine(store - result)

}
