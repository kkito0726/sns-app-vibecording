dependencies {
    implementation(project(":app:domain"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
