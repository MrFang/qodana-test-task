plugins {
    application
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    implementation("io.github.cdimascio:dotenv-java:2.2.0")
}

application {
    mainClass.set("testtask.App")
}