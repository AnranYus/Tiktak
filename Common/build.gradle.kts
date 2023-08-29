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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.3")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.0.6")
    implementation ("com.auth0:java-jwt:4.4.0")
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
    testCompileOnly("org.projectlombok:lombok:1.18.28")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}