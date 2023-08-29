plugins {
    id("java")
}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("com.auth0:java-jwt:4.4.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.3")
}

tasks.test {
    useJUnitPlatform()
}