group = "it.intre.kcc"
version = "1.0-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.2.31"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    compile ("org.slf4j:slf4j-api:1.7.5")
    compile ("org.slf4j:slf4j-log4j12:1.7.5")
    testCompile("junit", "junit", "4.12")
}

application {
    mainClassName = "it.intre.kcc.raffle.RaffleAppKt"
}
