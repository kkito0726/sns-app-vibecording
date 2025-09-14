dependencies {
    implementation(project(":app:domain"))
    implementation(project(":jooq-generator"))
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
}
