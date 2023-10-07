plugins {
    id("java")
    id("org.springframework.boot") version "3.0.6"

}

group = "com.anryus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")

    //Common作为公共依赖不需要编译产物
    if (this.name != "Common"){

        apply{
            plugin("org.springframework.boot")
        }

        tasks.bootJar{
            destinationDirectory.set(file("../build/target/"))
        }

    }

    dependencies {
        implementation("mysql:mysql-connector-java:8.0.33")
        implementation("org.springframework.cloud:spring-cloud-loadbalancer:4.0.4")

        compileOnly("org.projectlombok:lombok:1.18.28")
        annotationProcessor("org.projectlombok:lombok:1.18.28")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
        testCompileOnly("org.projectlombok:lombok:1.18.28")

        implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.4")
        implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.2")
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2022.0.0.0"){
            exclude("com.anryus","Common")
        }
        implementation("org.springframework.boot:spring-boot-starter-data-redis:3.0.6")
        implementation("org.springframework.boot:spring-boot-starter-web:3.0.6"){
            exclude("com.anryus","Gateway")
        }
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

}

//父项目不需要编译产物
tasks.bootJar{
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}