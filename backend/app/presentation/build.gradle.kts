dependencies {
    implementation(project(":app:usecase"))
}

springBoot {
    mainClass.set("com.example.flick.presentation.FlickApplicationKt")
}