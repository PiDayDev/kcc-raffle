package it.intre.kcc.raffle

data class Prize(val name: String, val descr: String, val image: String, val secret: String = "") {
    override fun toString() = "$name ($descr)"
}

val NONE = Prize("nothing", "", "http://no.image.png")