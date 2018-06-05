package it.intre.kcc.raffle

sealed class Raffle(val engine: Engine, val input: Input, val output: Output) {

    fun drawPrizes() {
        var e = engine
        while (e.hasMorePrizes()) {
            val result = e.drawPrize()
            output.print(result)
            e -= result
            input.read()
        }
    }

}

class TestRaffle : Raffle(ZeroSuspenseEngine(Store(MemoryDataSource())), StdInput(), StdOutput())

class ActualRaffle : Raffle(TrulyRandomEngine(Store(CsvDataSource())), StdInput(), StdOutput())

fun main(args: Array<String>) {
    val raffle = ActualRaffle()
    //TestRaffle()
    raffle.drawPrizes()
}


