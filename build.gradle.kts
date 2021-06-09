group = "it.intre"
version = "1.0-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.4.32"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib"))
    compile ("org.slf4j:slf4j-api:1.7.5")
    compile ("org.slf4j:slf4j-log4j12:1.7.5")
    testCompile("junit", "junit", "4.12")
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("it.intre.conf.raffle.RaffleAppKt")
}
