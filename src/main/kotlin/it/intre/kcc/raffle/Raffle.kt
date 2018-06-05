package it.intre.kcc.raffle

sealed class Setup {
    abstract fun setup(): Pair<Engine, Output>
}

class TestSetup : Setup() {
    override fun setup(): Pair<Engine, Output> {
        val dataSource = MemoryDataSource()
        val output = StdOutput()
        val store = Store(dataSource)
        val engine = ZeroSuspenseEngine(store)
        return Pair(engine, output)
    }
}


fun main(args: Array<String>) {

    val (engine, output) = TestSetup().setup()

    drawPrizes(engine, output)

}


private fun drawPrizes(engine: Engine, output: Output) {
    var e = engine
    while (e.hasMorePrizes()) {
        val result = e.drawPrize()
        output.print(result)
        e -= result
    }
}