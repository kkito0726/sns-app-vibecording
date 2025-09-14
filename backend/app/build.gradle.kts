plugins {
    kotlin("jvm") // Keep JVM plugin for Kotlin
}

description = "Flick SNS Application"

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework:spring-tx") // Explicitly add for @Transactional
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("io.minio:minio:8.5.10")
        implementation("com.mysql:mysql-connector-j")
        implementation("org.springframework.boot:spring-boot-starter-validation") // Already there
        implementation("org.reactivestreams:reactive-streams:1.0.4") // Already there

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.mockito:mockito-junit-jupiter")
    }
}
