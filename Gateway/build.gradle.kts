plugins {
    id("java")
}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2022.0.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.0.4")
    implementation("org.springframework.cloud:spring-cloud-loadbalancer:4.0.4")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.0.7")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.4")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2022.0.0.0")
    implementation(project(mapOf("path" to ":Common")))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}