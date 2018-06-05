package it.intre.kcc.raffle

abstract class Engine(val store: Store) {

    fun hasMorePrizes() = store.prizes.isNotEmpty() && store.attendees.isNotEmpty()

    abstract fun drawPrize(): Result

    abstract operator fun minus(result: Result): Engine
}

class ZeroSuspenseEngine(store: Store) : Engine(store) {

    override fun drawPrize(): Result = Result(store.attendees[0], store.prizes[0])

    override fun minus(result: Result) = ZeroSuspenseEngine(store - result)

}
