plugins {
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
}

dependencies {
    implementation(project(":app:usecase"))
    implementation(project(":app:domain"))
    val jwtVersion = "0.12.5"
    implementation("io.jsonwebtoken:jjwt-api:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jwtVersion}")
    testImplementation(project(":app:infra"))
    testImplementation(project(":jooq-generator"))
    testImplementation("org.springframework.boot:spring-boot-starter-jooq")
}

springBoot {
    mainClass.set("com.example.flick.presentation.FlickApplicationKt")
}

tasks.named("bootJar").configure {
    enabled = true
}