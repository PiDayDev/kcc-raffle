package it.intre.conf.raffle

data class Attendee(val firstName: String, val lastName: String, val mail: String) {
    override fun toString() = "$firstName $lastName"
}

val NOBODY = Attendee("Nobody", "Noone", "")