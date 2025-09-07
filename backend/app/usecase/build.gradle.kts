plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":app:domain"))
    implementation(project(":app:infra"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx") // Explicitly add for @Transactional

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
