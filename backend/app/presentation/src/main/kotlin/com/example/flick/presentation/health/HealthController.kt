package com.example.flick.presentation.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/health")
    fun healthCheck(): String {
        return "Flick backend app is running!!"
    }
}
