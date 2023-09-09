plugins {
    id("java")
    id("org.springframework.boot") version "3.0.6"
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

tasks.bootJar{
    enabled = true
    mainClass.set("com.anryus.relation.RelationApplication")
}