package it.intre.conf.raffle

sealed class Raffle(private val engine: Engine, private val inout: InputOutput) {

    fun drawPrizes() {
        while (engine.hasPrizes()) {
            val prize = engine.nextPrize()
            val winner = engine.nextWinner()
            inout.print(Result(winner, prize))
            inout.read()
        }
    }

}

class TestRaffle : Raffle(ZeroSuspenseEngine(Store(MemoryDataSource)), StdInOut)

class RndTestRaffle : Raffle(TrulyRandomEngine(Store(MemoryDataSource)), StdInOut)

class ActualRaffle : Raffle(TrulyRandomEngine(Store(CsvDataSource)), WindowOutput())

fun main(args: Array<String>) {
    val raffle =
            //ActualRaffle()
            RndTestRaffle()
    raffle.drawPrizes()
}


