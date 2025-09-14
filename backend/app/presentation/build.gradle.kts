plugins {
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
}

dependencies {
    implementation(project(":app:usecase"))
    testRuntimeOnly(project(":app:infra"))
    testImplementation(project(":jooq-generator"))
    testImplementation("org.springframework.boot:spring-boot-starter-jooq")
}

springBoot {
    mainClass.set("com.example.flick.presentation.FlickApplicationKt")
}