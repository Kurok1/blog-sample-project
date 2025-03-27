plugins {
    id("java")
}

group = "io.github.kurok1"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    // BOM
    implementation(platform(libs.spring.boot.dependencies))
    compileOnly("org.springframework.boot:spring-boot-starter-amqp")
}

tasks.test {
    useJUnitPlatform()
}