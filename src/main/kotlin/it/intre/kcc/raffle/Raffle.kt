package it.intre.kcc.raffle

sealed class Setup {
    abstract fun setup(): Pair<Engine, Output>
}

class TestSetup : Setup() {
    override fun setup() = ZeroSuspenseEngine(Store(MemoryDataSource())) to StdOutput()
}

class RealSetup:Setup(){
    override fun setup() = TrulyRandomEngine(Store(CsvDataSource())) to StdOutput()
}


fun main(args: Array<String>) {

    val testSetup =
            RealSetup()
            //TestSetup()

    val (engine, output) = testSetup.setup()

    drawPrizes(engine, output)

}


private fun drawPrizes(engine: Engine, output: Output) {
    var e = engine
    while (e.hasMorePrizes()) {
        val result = e.drawPrize()
        output.print(result)
        e -= result
        readLine()
    }
}