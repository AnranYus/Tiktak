plugins {
    id("java")
}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    if (this.name != "Gateway"){
        afterEvaluate {
            dependencies {

                implementation("mysql:mysql-connector-java:8.0.33")
                implementation("org.springframework.cloud:spring-cloud-loadbalancer:4.0.4")

                compileOnly("org.projectlombok:lombok:1.18.28")
                annotationProcessor("org.projectlombok:lombok:1.18.28")
                testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
                testCompileOnly("org.projectlombok:lombok:1.18.28")

                implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.4")
                implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.2")
                implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2022.0.0.0")
                implementation("org.springframework.boot:spring-boot-starter-data-redis:3.0.6")
                implementation("org.springframework.boot:spring-boot-starter-web:3.0.6")
                testImplementation(platform("org.junit:junit-bom:5.9.1"))
                testImplementation("org.junit.jupiter:junit-jupiter")
            }

        }
    }

}

tasks.test {
    useJUnitPlatform()
}