plugins {
    id("org.springframework.boot") // Add back Spring Boot plugin
    kotlin("plugin.spring")
    kotlin("jvm") // Keep JVM plugin for Kotlin
}

dependencies {
    implementation(project(":app:usecase"))
    implementation(project(":jooq-generator")) // Add jooq-generator dependency here
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.minio:minio:8.5.10")
    implementation("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-validation") // Already there
    implementation("org.reactivestreams:reactive-streams:1.0.4") // Already there

    // Test dependencies (if needed for presentation module tests)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

springBoot {
    mainClass.set("com.example.flick.presentation.FlickApplicationKt")
}