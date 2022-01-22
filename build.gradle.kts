plugins {
    kotlin("jvm") version "1.5.10"
}

group = "top.hoshino9"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}