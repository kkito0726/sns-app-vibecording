dependencies {
    implementation(project(":app:domain"))
    val jwtVersion = "0.12.5"
    implementation("io.jsonwebtoken:jjwt-api:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jwtVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
