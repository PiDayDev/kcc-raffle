package it.intre.conf.raffle

data class Prize(val pos: Int, val name: String, val descr: String, val image: String, val secret: String = "") {
    override fun toString() = "$name ($descr)"
}

val NONE = Prize(0, "nothing", "", "http://no.image.png")