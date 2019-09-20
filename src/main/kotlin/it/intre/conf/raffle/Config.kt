package it.intre.conf.raffle

import java.util.*


class Config {

    private val properties: Properties by lazy {
        val p = Properties()
        p.load(ClassLoader.getSystemResource("config.properties").openStream())
        p
    }
    val appTitle: String by lazy { properties.getProperty("app.title") }
    val welcomeTitle: String by lazy { properties.getProperty("welcome.title") }
    val welcomeLogo: String by lazy { properties.getProperty("welcome.logo") }
    val welcomeNote: String by lazy { properties.getProperty("welcome.note") }
}