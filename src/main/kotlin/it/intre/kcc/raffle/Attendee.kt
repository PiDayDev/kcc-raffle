package it.intre.kcc.raffle

data class Attendee(val firstName: String, val lastName: String, val mail: String)

val NOBODY = Attendee("Nobody", "Noone", "")