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
    testCompile("junit", "junit", "4.12")
}

application {
    mainClassName = "it.intre.kcc.raffle.RaffleKt"
}
