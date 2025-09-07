plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework:spring-context")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
