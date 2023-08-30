plugins {
    id("java")
}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp:3.1.3")
    implementation("software.amazon.awssdk:s3:2.20.131")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2022.0.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.0.4")
    implementation(project(mapOf("path" to ":Common")))
}

tasks.test {
    useJUnitPlatform()
}