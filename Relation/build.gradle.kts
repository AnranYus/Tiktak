plugins {
    id("java")
}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(mapOf("path" to ":Common")))
}

tasks.test {
    useJUnitPlatform()
}