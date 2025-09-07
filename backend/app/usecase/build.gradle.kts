plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app:domain"))
    implementation(project(":app:infra"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx") // Explicitly add for @Transactional
}
