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
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2022.0.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.0.4")
    implementation(project(mapOf("path" to ":Common")))
    implementation ("com.auth0:java-jwt:4.4.0")
}


tasks.test {
    useJUnitPlatform()
}